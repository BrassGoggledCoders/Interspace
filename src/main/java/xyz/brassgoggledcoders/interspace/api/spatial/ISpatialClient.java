package xyz.brassgoggledcoders.interspace.api.spatial;

import com.mojang.datafixers.util.Either;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.query.SpatialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ISpatialClient {
    CompletableFuture<Integer> setupWorld(World world);

    Transaction<Collection<SpatialItem>> offer(World world, ChunkPos chunkPos, Collection<SpatialItem> offered);

    Transaction<Collection<SpatialItem>> query(World world, SpatialQueryBuilder spatialQueryBuilder);

    Transaction<Collection<SpatialItem>> retrieve(World world, SpatialQueryBuilder spatialQueryBuilder);

    Transaction<Either<String, Integer>> cancel(World world, UUID transactionIdToCancel);
}
