package xyz.brassgoggledcoders.interspace.api.spacial;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceInsert;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceQuery;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IInterspaceClient {
    CompletableFuture<Integer> setupWorld(IWorld world);

    CompletableFuture<Pair<ChunkPos, SpacialInstance>> setupChunk(IWorld world, IChunk chunk);

    CompletableFuture<List<SpacialItem>> query(InterspaceQuery query);

    CompletableFuture<Integer> insert(InterspaceInsert insert);

    CompletableFuture<List<SpacialItem>> remove(InterspaceQuery query);
}
