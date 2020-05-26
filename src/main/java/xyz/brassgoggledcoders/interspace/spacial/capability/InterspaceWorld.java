package xyz.brassgoggledcoders.interspace.spacial.capability;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspace;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceInsert;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceQuery;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpacialTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class InterspaceWorld implements IInterspace {
    private final CompletableFuture<Void> worldSetup;

    private final IWorld world;
    private final Map<ChunkPos, SpacialInstance> activeChunks;
    private final List<ChunkPos> inactiveChunks;

    public InterspaceWorld(IWorld world) {
        this.world = world;
        this.worldSetup = InterspaceAPI.getInterspaceClient()
                .setupWorld(world)
                .thenAccept(amount -> Interspace.LOGGER.debug("Added {} tables", amount));
        this.activeChunks = Maps.newHashMap();
        this.inactiveChunks = Lists.newArrayList();
    }

    @Override
    public void tick() {
        if (worldSetup.isDone()) {
            activeChunks.values().forEach(SpacialInstance::tick);
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
        ChunkPos chunkPos = chunk.getPos();
        inactiveChunks.remove(chunkPos);
        InterspaceAPI.getInterspaceClient()
                .setupChunk(world, chunk)
                .thenAccept(this::acceptSpacialPair);
    }

    private void acceptSpacialPair(Pair<ChunkPos, SpacialInstance> spacialPair) {
        if (!this.inactiveChunks.contains(spacialPair.getKey())) {
            this.activeChunks.put(spacialPair.getKey(), spacialPair.getValue());
            spacialPair.getRight().onLoad();
        } else {
            this.inactiveChunks.remove(spacialPair.getKey());
        }
    }

    @Override
    public void onChunkUnload(@Nonnull IChunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        SpacialInstance spacialInstance = activeChunks.remove(chunkPos);
        if (spacialInstance == null) {
            inactiveChunks.add(chunkPos);
        }
    }

    @Nullable
    @Override
    public SpacialInstance getSpacialInstance(ChunkPos chunkPos) {
        return activeChunks.get(chunkPos);
    }


}
