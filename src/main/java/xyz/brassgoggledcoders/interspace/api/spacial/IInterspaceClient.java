package xyz.brassgoggledcoders.interspace.api.spacial;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.SpacialQuery;
import xyz.brassgoggledcoders.interspace.api.spacial.query.Transaction;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface IInterspaceClient {
    CompletableFuture<Integer> setupWorld(IWorld world);

    Transaction<Collection<SpacialItem>> offer(IWorld world, ChunkPos chunkPos, Collection<SpacialItem> offered);

    Transaction<Collection<SpacialItem>> query(IWorld world, SpacialQuery spacialQuery);

    Transaction<Collection<SpacialItem>> retrieve(IWorld world, SpacialQuery spacialQuery);
}
