package xyz.brassgoggledcoders.interspace.api.spacial.entry;

import net.minecraft.nbt.CompoundNBT;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialType;

import javax.annotation.Nullable;

public class SpacialEntry {
    private final SpacialType type;
    private final double weight;
    private final CompoundNBT nbt;

    public SpacialEntry(SpacialType type, double weight, @Nullable CompoundNBT nbt) {
        this.type = type;
        this.weight = weight;
        this.nbt = nbt;
    }

    public SpacialType getType() {
        return type;
    }

    public double getWeight() {
        return weight;
    }

    public CompoundNBT getNBT() {
        return nbt;
    }
}
