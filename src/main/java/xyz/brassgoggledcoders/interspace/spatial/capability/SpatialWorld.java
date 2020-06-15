package xyz.brassgoggledcoders.interspace.spatial.capability;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.InterspaceRegistries;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialWorld;
import xyz.brassgoggledcoders.interspace.api.spatial.entry.SpatialEntry;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.query.SpatialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialInstance;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpatialTypes;
import xyz.brassgoggledcoders.interspace.util.NBTHelper;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SpatialWorld implements ISpatialWorld {
    private final ChunkPos ORIGIN = new ChunkPos(0, 0);
    private final CompletableFuture<Void> worldSetup;

    private final IWorld world;
    private final Set<ChunkPos> activeChunks;
    private final Map<ChunkPos, SpatialInstance> chunks;
    private final Set<ChunkPos> awaitingLoad;

    public SpatialWorld(IWorld world) {
        this.world = world;
        this.worldSetup = InterspaceAPI.getInterspaceClient()
                .setupWorld(world)
                .thenAccept(amount -> Interspace.LOGGER.debug("Added {} tables", amount));
        this.activeChunks = Sets.newHashSet();
        this.chunks = Maps.newHashMap();
        this.chunks.put(ORIGIN, InterspaceSpatialTypes.EMPTY.get().createInstance(world, ORIGIN));
        this.awaitingLoad = Sets.newHashSet();
    }

    @Override
    public void tick() {
        if (worldSetup.isDone()) {
            if (!awaitingLoad.isEmpty()) {
                for (ChunkPos chunkPos: awaitingLoad) {
                    SpatialInstance spatialInstance = chunks.get(chunkPos);
                    if (spatialInstance != null) {
                        spatialInstance.onLoad();
                    }
                }
            }
            for (Map.Entry<ChunkPos, SpatialInstance> spacialInstanceEntry : chunks.entrySet()) {
                if (activeChunks.contains(spacialInstanceEntry.getKey())) {
                    spacialInstanceEntry.getValue().tick();
                }
            }
        }
    }

    @Override
    public void onChunkLoad(@Nonnull IChunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        activeChunks.add(chunkPos);
        if (chunks.containsKey(chunkPos)) {
            if (worldSetup.isDone()) {
                chunks.get(chunkPos).onLoad();
            } else {
                awaitingLoad.add(chunkPos);
            }
        }
    }

    @Override
    public void onChunkUnload(@Nonnull IChunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        activeChunks.remove(chunkPos);
        awaitingLoad.remove(chunkPos);
    }

    @Override
    @Nonnull
    public SpatialInstance getSpacialInstance(ChunkPos chunkPos) {
        SpatialInstance spatialInstance = chunks.get(chunkPos);
        if (!world.isRemote() && spatialInstance == null) {
            SpatialEntry spatialEntry = InterspaceAPI.getSpacialEntryManager().getRandomSpatialEntryFor(world,
                    SharedSeedRandom.seedSlimeChunk(chunkPos.x, chunkPos.z, world.getSeed(), 831129799101L));
            spatialInstance = spatialEntry.getType().createInstance(world, chunkPos);
            if (spatialEntry.getNBT() != null) {
                spatialInstance.deserializeNBT(spatialEntry.getNBT());
            }
            chunks.put(chunkPos, spatialInstance);
            if (worldSetup.isDone()) {
                spatialInstance.onLoad();
            } else {
                awaitingLoad.add(chunkPos);
            }
        }
        return spatialInstance;
    }


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        ListNBT spacialInstancesNBT = new ListNBT();
        for (Map.Entry<ChunkPos, SpatialInstance> spacialInstanceEntry : chunks.entrySet()) {
            CompoundNBT spacialInstanceNBT = new CompoundNBT();
            SpatialInstance spatialInstance = spacialInstanceEntry.getValue();
            spacialInstanceNBT.putString("type", Objects.requireNonNull(spatialInstance.getType().getRegistryName()).toString());
            spacialInstanceNBT.put("nbt", spatialInstance.serializeNBT());
            spacialInstanceNBT.putLong("chunkPos", spacialInstanceEntry.getKey().asLong());
            spacialInstancesNBT.add(spacialInstanceNBT);
        }
        compoundNBT.put("spacialInstances", spacialInstancesNBT);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        Collection<SpatialInstance> spatialInstances = NBTHelper.fromListNBT(nbt,
                "spacialInstances", this::parseSpacialInstances);
        spatialInstances.forEach(value -> chunks.put(value.getChunkPos(), value));
    }

    private SpatialInstance parseSpacialInstances(CompoundNBT compoundNBT) {
        SpatialType type = InterspaceRegistries.SPACIAL_TYPES.getValue(new ResourceLocation(compoundNBT.getString("type")));
        ChunkPos chunkPos = new ChunkPos(compoundNBT.getLong("chunkPos"));
        if (type != null) {
            SpatialInstance spatialInstance = type.createInstance(world, chunkPos);
            spatialInstance.deserializeNBT(compoundNBT.getCompound("nbt"));
            return spatialInstance;
        } else {
            return null;
        }
    }

    @Override
    public Transaction<Collection<SpatialItem>> offer(ChunkPos chunkPos, Collection<SpatialItem> offered) {
        return InterspaceAPI.getInterspaceClient().offer(world, chunkPos, offered);
    }

    @Override
    public Transaction<Collection<SpatialItem>> offer(Collection<SpatialItem> offered) {
        return this.offer(ORIGIN, offered);
    }

    @Override
    public Transaction<Collection<SpatialItem>> query(SpatialQueryBuilder spatialQueryBuilder) {
        return InterspaceAPI.getInterspaceClient().query(world, spatialQueryBuilder);
    }

    @Override
    public Transaction<Collection<SpatialItem>> retrieve(SpatialQueryBuilder spatialQueryBuilder) {
        return InterspaceAPI.getInterspaceClient().retrieve(world, spatialQueryBuilder);

    }
}
