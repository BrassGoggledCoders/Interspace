package xyz.brassgoggledcoders.interspace.api.spacial;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;

import javax.annotation.Nonnull;

public interface IInterspace extends INBTSerializable<CompoundNBT> {
    void tick();

    void onChunkLoad(@Nonnull IChunk chunk);

    void onChunkUnload(@Nonnull IChunk chunk);

    @Nonnull
    SpacialInstance getSpacialInstance(ChunkPos chunk);
}
