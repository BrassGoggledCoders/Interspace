package xyz.brassgoggledcoders.interspace.spacial.capability;

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
import xyz.brassgoggledcoders.interspace.api.spacial.capability.IInterspaceWorld;
import xyz.brassgoggledcoders.interspace.api.spacial.entry.SpacialEntry;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.SpacialQuery;
import xyz.brassgoggledcoders.interspace.api.spacial.query.Transaction;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialType;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpacialTypes;
import xyz.brassgoggledcoders.interspace.util.NBTHelper;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class InterspaceWorld implements IInterspaceWorld {
    private final ChunkPos ORIGIN = new ChunkPos(0, 0);
    private final CompletableFuture<Void> worldSetup;

    private final IWorld world;
    private final Set<ChunkPos> activeChunks;
    private final Map<ChunkPos, SpacialInstance> chunks;
    private final Set<ChunkPos> awaitingLoad;

    public InterspaceWorld(IWorld world) {
        this.world = world;
        this.worldSetup = InterspaceAPI.getInterspaceClient()
                .setupWorld(world)
                .thenAccept(amount -> Interspace.LOGGER.debug("Added {} tables", amount));
        this.activeChunks = Sets.newHashSet();
        this.chunks = Maps.newHashMap();
        this.chunks.put(ORIGIN, InterspaceSpacialTypes.EMPTY.get().createInstance(world, ORIGIN));
        this.awaitingLoad = Sets.newHashSet();
    }

    @Override
    public void tick() {
        if (worldSetup.isDone()) {
            if (!awaitingLoad.isEmpty()) {
                for (ChunkPos chunkPos: awaitingLoad) {
                    SpacialInstance spacialInstance = chunks.get(chunkPos);
                    if (spacialInstance != null) {
                        spacialInstance.onLoad();
                    }
                }
            }
            for (Map.Entry<ChunkPos, SpacialInstance> spacialInstanceEntry : chunks.entrySet()) {
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
    public SpacialInstance getSpacialInstance(ChunkPos chunkPos) {
        SpacialInstance spacialInstance = chunks.get(chunkPos);
        if (!world.isRemote() && spacialInstance == null) {
            SpacialEntry spacialEntry = InterspaceAPI.getSpacialEntryManager().getRandomSpacialEntryFor(world,
                    SharedSeedRandom.seedSlimeChunk(chunkPos.x, chunkPos.z, world.getSeed(), 831129799101L));
            spacialInstance = spacialEntry.getType().createInstance(world, chunkPos);
            if (spacialEntry.getNBT() != null) {
                spacialInstance.deserializeNBT(spacialEntry.getNBT());
            }
            chunks.put(chunkPos, spacialInstance);
            if (worldSetup.isDone()) {
                spacialInstance.onLoad();
            } else {
                awaitingLoad.add(chunkPos);
            }
        }
        return spacialInstance;
    }


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        ListNBT spacialInstancesNBT = new ListNBT();
        for (Map.Entry<ChunkPos, SpacialInstance> spacialInstanceEntry : chunks.entrySet()) {
            CompoundNBT spacialInstanceNBT = new CompoundNBT();
            SpacialInstance spacialInstance = spacialInstanceEntry.getValue();
            spacialInstanceNBT.putString("type", Objects.requireNonNull(spacialInstance.getType().getRegistryName()).toString());
            spacialInstanceNBT.put("nbt", spacialInstance.serializeNBT());
            spacialInstanceNBT.putLong("chunkPos", spacialInstanceEntry.getKey().asLong());
            spacialInstancesNBT.add(spacialInstanceNBT);
        }
        compoundNBT.put("spacialInstances", spacialInstancesNBT);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        Collection<SpacialInstance> spacialInstances = NBTHelper.fromListNBT(nbt,
                "spacialInstances", this::parseSpacialInstances);
        spacialInstances.forEach(value -> chunks.put(value.getChunkPos(), value));
    }

    private SpacialInstance parseSpacialInstances(CompoundNBT compoundNBT) {
        SpacialType type = InterspaceRegistries.SPACIAL_TYPES.getValue(new ResourceLocation(compoundNBT.getString("type")));
        ChunkPos chunkPos = new ChunkPos(compoundNBT.getLong("chunkPos"));
        if (type != null) {
            SpacialInstance spacialInstance = type.createInstance(world, chunkPos);
            spacialInstance.deserializeNBT(compoundNBT.getCompound("nbt"));
            return spacialInstance;
        } else {
            return null;
        }
    }

    @Override
    public Transaction<Collection<SpacialItem>> offer(ChunkPos chunkPos, Collection<SpacialItem> offered) {
        return InterspaceAPI.getInterspaceClient().offer(world, chunkPos, offered);
    }

    @Override
    public Transaction<Collection<SpacialItem>> offer(Collection<SpacialItem> offered) {
        return this.offer(ORIGIN, offered);
    }

    @Override
    public Transaction<Collection<SpacialItem>> query(SpacialQuery spacialQuery) {
        return InterspaceAPI.getInterspaceClient().query(world, spacialQuery);
    }

    @Override
    public Transaction<Collection<SpacialItem>> retrieve(SpacialQuery spacialQuery) {
        return InterspaceAPI.getInterspaceClient().retrieve(world, spacialQuery);

    }
}
