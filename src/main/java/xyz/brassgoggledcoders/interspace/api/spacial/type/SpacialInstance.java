package xyz.brassgoggledcoders.interspace.api.spacial.type;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class SpacialInstance implements INBTSerializable<CompoundNBT> {
    private final SpacialType spacialType;

    public SpacialInstance(SpacialType spacialType) {
        this.spacialType = spacialType;
    }

    public void tick() {

    }


    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
