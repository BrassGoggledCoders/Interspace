package xyz.brassgoggledcoders.interspace.spatial.capability;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullLazy;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialChunk;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialWorld;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.query.SpatialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialInstance;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpatialTypes;

import javax.annotation.Nonnull;
import java.util.Collection;

public class SpatialChunk implements ISpatialChunk {
    private final NonNullLazy<LazyOptional<ISpatialWorld>> worldProvider;
    private final NonNullLazy<SpatialInstance> spacialProvider;

    public SpatialChunk(World world, ChunkPos chunkPos) {
        worldProvider = NonNullLazy.of(() -> world.getCapability(InterspaceAPI.INTERSPACE_WORLD));
        spacialProvider = NonNullLazy.of(() -> worldProvider.get()
                .map(interspaceWorld -> interspaceWorld.getSpacialInstance(chunkPos))
                .orElseGet(() -> InterspaceSpatialTypes.EMPTY.get().createInstance(world, chunkPos)));
    }

    @Override
    @Nonnull
    public SpatialInstance getSpacialInstance() {
        return spacialProvider.get();
    }

    @Override
    public Transaction<Collection<SpatialItem>> offer(Collection<SpatialItem> offered) {
        return this.getSpacialInstance().offer(offered);
    }

    @Override
    public Transaction<Collection<SpatialItem>> query(SpatialQueryBuilder spatialQueryBuilder) {
        return this.getSpacialInstance().query(spatialQueryBuilder);
    }

    @Override
    public Transaction<Collection<SpatialItem>> retrieve(SpatialQueryBuilder spatialQueryBuilder) {
        return this.getSpacialInstance().retrieve(spatialQueryBuilder);
    }
}
