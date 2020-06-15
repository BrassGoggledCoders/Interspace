package xyz.brassgoggledcoders.interspace.spatial.instance;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.query.SpatialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialInstance;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;

import java.util.Collection;
import java.util.Collections;

public class EmptySpatialInstance extends SpatialInstance {
    public EmptySpatialInstance(SpatialType spatialType, IWorld world, ChunkPos chunkPos) {
        super(spatialType, world, chunkPos);
    }

    @Override
    public Transaction<Collection<SpatialItem>> offer(Collection<SpatialItem> offered) {
        return Transaction.of(offered);
    }

    @Override
    public Transaction<Collection<SpatialItem>> query(SpatialQueryBuilder spatialQueryBuilder) {
        return Transaction.of(Collections.emptyList());
    }

    @Override
    public Transaction<Collection<SpatialItem>> retrieve(SpatialQueryBuilder spatialQueryBuilder) {
        return Transaction.of(Collections.emptyList());
    }
}
