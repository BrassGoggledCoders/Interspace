package xyz.brassgoggledcoders.interspace.api.spacial;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.parameter.SpacialParameter;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IInterspaceClient {
    CompletableFuture<Integer> setupWorld(IWorld world);

    CompletableFuture<Collection<SpacialItem>> offer(UUID transactionId, IWorld world, ChunkPos chunkPos,
                                                     Collection<SpacialItem> offered);

    CompletableFuture<Collection<SpacialItem>> query(IWorld world, Collection<SpacialParameter<?>> parameters,
                                                     @Nullable Integer limit);

    CompletableFuture<Collection<SpacialItem>> retrieve(UUID transactionId, IWorld world,
                                                        Collection<SpacialParameter<?>> parameters,
                                                        @Nullable Integer limit);
}
