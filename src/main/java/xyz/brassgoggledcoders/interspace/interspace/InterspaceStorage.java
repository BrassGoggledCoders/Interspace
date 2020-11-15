package xyz.brassgoggledcoders.interspace.interspace;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.storage.WorldSavedData;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspace;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceStorage;

import javax.annotation.Nonnull;

public class InterspaceStorage extends WorldSavedData implements IInterspaceStorage {
    public InterspaceStorage() {
        super(InterspaceMod.ID);
    }

    @Override
    public void read(@Nonnull CompoundNBT nbt) {

    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        return new CompoundNBT();
    }

    @Override
    public IInterspace getInterspace(@Nonnull ChunkPos chunkPos) {
        return null;
    }
}
