package xyz.brassgoggledcoders.interspace.spacial.capability;

import com.google.common.collect.Lists;
import net.minecraft.world.chunk.IChunk;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspace;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceInsert;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceQuery;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceRemove;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class InterspaceWorld implements IInterspace {

    @Override
    public void tick() {

    }

    @Override
    @Nonnull
    public Future<List<SpacialItem>> query(@Nonnull InterspaceQuery query) {
        return CompletableFuture.completedFuture(Lists.newArrayList());
    }

    @Override
    @Nonnull
    public <T> Future<T> remove(@Nonnull InterspaceRemove<T> interspaceRemove) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Nonnull
    public <T> Future<Integer> insert(@Nonnull InterspaceInsert<T> interspaceInsert) {
        return CompletableFuture.completedFuture(0);
    }

    @Override
    public void onChunkLoad(@Nonnull IChunk chunk) {

    }

    @Override
    public void onChunkUnload(@Nonnull IChunk chunk) {

    }
}
