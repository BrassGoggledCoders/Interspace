package xyz.brassgoggledcoders.interspace.spacial.capability;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspace;
import xyz.brassgoggledcoders.interspace.api.spacial.entry.SpacialEntry;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceInsert;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceQuery;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpacialTypes;
import xyz.brassgoggledcoders.interspace.json.SpacialEntryManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class InterspaceWorld implements IInterspace {
    private final CompletableFuture<Void> worldSetup;

    private final IWorld world;
    private final Set<ChunkPos> activeChunks;
    private final Map<ChunkPos, SpacialInstance> chunks;

    public InterspaceWorld(IWorld world) {
        this.world = world;
        this.worldSetup = InterspaceAPI.getInterspaceClient()
                .setupWorld(world)
                .thenAccept(amount -> Interspace.LOGGER.debug("Added {} tables", amount));
        this.activeChunks = Sets.newHashSet();
        this.chunks = Maps.newHashMap();
    }

    @Override
    public void tick() {
        if (worldSetup.isDone()) {
            for (Map.Entry<ChunkPos, SpacialInstance> spacialInstanceEntry : chunks.entrySet()) {
                if (activeChunks.contains(spacialInstanceEntry.getKey())) {
                    spacialInstanceEntry.getValue().tick();
                }
            }
        }
    }

    @Override
    @Nonnull
    public CompletableFuture<List<SpacialItem>> query(@Nonnull InterspaceQuery query) {
        if (worldSetup.isDone()) {
            return InterspaceAPI.getInterspaceClient().query(query);
        } else {
            return CompletableFuture.completedFuture(Lists.newArrayList());
        }
    }

    @Override
    @Nonnull
    public CompletableFuture<List<SpacialItem>> remove(@Nonnull InterspaceQuery query) {
        return CompletableFuture.completedFuture(Lists.newArrayList());
    }

    @Override
    @Nonnull
    public CompletableFuture<Integer> insert(@Nonnull InterspaceInsert interspaceInsert) {
        return InterspaceAPI.getInterspaceClient().insert(interspaceInsert);
    }

    @Override
    public void onChunkLoad(@Nonnull IChunk chunk) {
        activeChunks.add(chunk.getPos());
    }

    @Override
    public void onChunkUnload(@Nonnull IChunk chunk) {
        activeChunks.remove(chunk.getPos());
    }

    @Override
    @Nonnull
    public SpacialInstance getSpacialInstance(IChunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        SpacialInstance spacialInstance = chunks.get(chunkPos);
        if (!world.isRemote() && spacialInstance == null) {
            SpacialEntry spacialEntry = InterspaceAPI.getSpacialEntryManager().getRandomSpacialEntryFor(world,
                    SharedSeedRandom.seedSlimeChunk(chunkPos.x, chunkPos.z, world.getSeed(), 831129799101L));
            spacialInstance = spacialEntry.getType().createInstance(world, chunk);
            if (spacialEntry.getNBT() != null) {
                spacialInstance.deserializeNBT(spacialEntry.getNBT());
            }
            chunks.put(chunkPos, spacialInstance);
        }
        return spacialInstance;
    }


    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
