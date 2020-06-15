package xyz.brassgoggledcoders.interspace.spacial.type;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialType;

public class StorageSpacialInstance extends SpacialInstance {
    public StorageSpacialInstance(SpacialType spacialType, IWorld world, ChunkPos chunkPos) {
        super(spacialType, world, chunkPos);
    }
}
