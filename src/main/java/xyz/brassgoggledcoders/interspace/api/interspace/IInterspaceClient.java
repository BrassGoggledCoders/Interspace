package xyz.brassgoggledcoders.interspace.api.interspace;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;

import java.util.concurrent.CompletableFuture;

public interface IInterspaceClient {
    CompletableFuture<Interspace> query(ResourceLocation world, ChunkPos chunkPos);
}
