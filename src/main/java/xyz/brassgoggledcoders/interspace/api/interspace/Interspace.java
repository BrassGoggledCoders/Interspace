package xyz.brassgoggledcoders.interspace.api.interspace;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;

public class Interspace {
    private final ResourceLocation name;
    private final int maxSize;
    private final ChunkPos chunkPos;

    public Interspace(ResourceLocation name, int maxSize, ChunkPos chunkPos) {
        this.name = name;
        this.maxSize = maxSize;
        this.chunkPos = chunkPos;
    }

    public ResourceLocation getName() {
        return name;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }
}
