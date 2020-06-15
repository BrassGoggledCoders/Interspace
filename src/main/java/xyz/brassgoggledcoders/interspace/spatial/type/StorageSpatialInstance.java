package xyz.brassgoggledcoders.interspace.spatial.type;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialInstance;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;

public class StorageSpatialInstance extends SpatialInstance {
    public StorageSpatialInstance(SpatialType spatialType, IWorld world, ChunkPos chunkPos) {
        super(spatialType, world, chunkPos);
    }
}
