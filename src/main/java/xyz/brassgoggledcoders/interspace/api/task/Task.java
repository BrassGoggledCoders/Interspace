package xyz.brassgoggledcoders.interspace.api.task;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class Task implements INBTSerializable<CompoundNBT> {

    public abstract Object getResult();

    public abstract boolean isDone();

    /**
     * @return true if this task should be completed before any others are handled (Generally reserved for World setup)
     */
    public boolean isBlocking() {
        return false;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }
}
