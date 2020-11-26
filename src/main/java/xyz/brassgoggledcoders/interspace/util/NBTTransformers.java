package xyz.brassgoggledcoders.interspace.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;

public class NBTTransformers {
    public static CompoundNBT fromChunkPos(ChunkPos chunkPos) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("x", chunkPos.x);
        nbt.putInt("z", chunkPos.z);
        return nbt;
    }

    public static ChunkPos toChunkPos(CompoundNBT nbt) {
        return new ChunkPos(
                nbt.getInt("x"),
                nbt.getInt("z")
        );
    }
}
