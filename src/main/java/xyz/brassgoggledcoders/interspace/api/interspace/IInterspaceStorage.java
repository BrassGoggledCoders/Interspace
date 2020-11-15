package xyz.brassgoggledcoders.interspace.api.interspace;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public interface IInterspaceStorage {
    IInterspace getInterspace(ChunkPos chunkPos);

    default IInterspace getInterspace(BlockPos blockPos) {
        return this.getInterspace(new ChunkPos(blockPos));
    }
}
