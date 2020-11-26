package xyz.brassgoggledcoders.interspace.interspace;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.util.NonNullFunction;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceClient;
import xyz.brassgoggledcoders.interspace.api.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.sql.ISQLClient;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.IntSupplier;

public class InterspaceClient implements IInterspaceClient {
    private final ISQLClient sqlClient;
    private final Map<ResourceLocation, LoadingCache<ChunkPos, Interspace>> interspaceCache;

    public InterspaceClient(ISQLClient sqlClient) {
        this.sqlClient = sqlClient;
        this.interspaceCache = Maps.newConcurrentMap();
    }

    @Override
    public CompletableFuture<Interspace> query(ResourceLocation world, ChunkPos chunkPos) {
        return CompletableFuture.supplyAsync(() ->
                interspaceCache.computeIfAbsent(world, this::createWorldCache).getUnchecked(chunkPos)
        );
    }

    private LoadingCache<ChunkPos, Interspace> createWorldCache(ResourceLocation world) {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .build(new InterspaceCacheLoader(sqlClient, world));
    }
}
