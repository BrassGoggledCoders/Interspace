package xyz.brassgoggledcoders.interspace.api.spacial;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceInsert;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceQuery;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceRemove;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface IInterspace {
    void tick();

    @Nonnull
    CompletableFuture<List<SpacialItem>> query(@Nonnull InterspaceQuery query);

    @Nonnull
    CompletableFuture<List<SpacialItem>> remove(@Nonnull InterspaceQuery query);

    @Nonnull
    CompletableFuture<Integer> insert(@Nonnull InterspaceInsert interspaceInsert);

    void onChunkLoad(@Nonnull IChunk chunk);

    void onChunkUnload(@Nonnull IChunk chunk);

    @Nullable
    SpacialInstance getSpacialInstance(ChunkPos chunkPos);
}
