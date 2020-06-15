package xyz.brassgoggledcoders.interspace.api.spatial;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.query.SpatialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface ISpatialClient {
    CompletableFuture<Integer> setupWorld(IWorld world);

    Transaction<Collection<SpatialItem>> offer(IWorld world, ChunkPos chunkPos, Collection<SpatialItem> offered);

    Transaction<Collection<SpatialItem>> query(IWorld world, SpatialQueryBuilder spatialQueryBuilder);

    Transaction<Collection<SpatialItem>> retrieve(IWorld world, SpatialQueryBuilder spatialQueryBuilder);
}
