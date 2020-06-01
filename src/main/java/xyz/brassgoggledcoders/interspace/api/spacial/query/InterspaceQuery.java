package xyz.brassgoggledcoders.interspace.api.spacial.query;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;

import java.util.Map;

public class InterspaceQuery {
    private final IWorld world;
    private final ChunkPos chunkPos;
    private final Map<String, String> markers;

    public InterspaceQuery(IWorld world, ChunkPos chunkPos, Map<String, String> markers) {
        this.world = world;
        this.chunkPos = chunkPos;
        this.markers = markers;
    }

    public IWorld getWorld() {
        return world;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public Map<String, String> getMarkers() {
        return markers;
    }
}
