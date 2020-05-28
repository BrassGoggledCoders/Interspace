package xyz.brassgoggledcoders.interspace.api.spacial.query;

import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;

import java.util.Map;

public class InterspaceQuery {
    private final IWorld world;
    private final IChunk chunk;
    private final Map<String, String> markers;

    public InterspaceQuery(IWorld world, IChunk chunk, Map<String, String> markers) {
        this.world = world;
        this.chunk = chunk;
        this.markers = markers;
    }

    public IWorld getWorld() {
        return world;
    }

    public IChunk getChunk() {
        return chunk;
    }

    public Map<String, String> getMarkers() {
        return markers;
    }
}
