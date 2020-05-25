package xyz.brassgoggledcoders.interspace.api.spacial;

import net.minecraft.world.chunk.IChunk;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceInsert;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceQuery;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceRemove;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.Future;

public interface IInterspace {
    void tick();

    @Nonnull
    Future<List<SpacialItem>> query(@Nonnull InterspaceQuery query);

    @Nonnull
    <T> Future<T> remove(@Nonnull InterspaceRemove<T> interspaceRemove);

    @Nonnull
    <T> Future<Integer> insert(@Nonnull InterspaceInsert<T> interspaceInsert);

    void onChunkLoad(@Nonnull IChunk chunk);

    void onChunkUnload(@Nonnull IChunk chunk);
}
