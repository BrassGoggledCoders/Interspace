package xyz.brassgoggledcoders.interspace.spacial;

import com.google.common.collect.Lists;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.interspace.api.IInterspaceClient;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceQuery;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpacialTypes;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class InterspaceClient implements IInterspaceClient {
    @Override
    public CompletableFuture<Integer> setupWorld(IWorld world) {
        return CompletableFuture.completedFuture(0);
    }

    @Override
    public CompletableFuture<Pair<ChunkPos, SpacialInstance>> setupChunk(IChunk chunk) {
        return CompletableFuture.completedFuture(Pair.of(chunk.getPos(), InterspaceSpacialTypes.EMPTY.get().createInstance()));
    }

    @Override
    public Future<List<SpacialItem>> query(InterspaceQuery query) {
        return CompletableFuture.completedFuture(Lists.newArrayList());
    }
}
