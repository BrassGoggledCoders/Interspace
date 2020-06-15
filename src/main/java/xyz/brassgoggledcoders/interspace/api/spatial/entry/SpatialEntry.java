package xyz.brassgoggledcoders.interspace.api.spatial.entry;

import net.minecraft.nbt.CompoundNBT;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;

import javax.annotation.Nullable;

public class SpatialEntry {
    private final SpatialType type;
    private final double weight;
    private final CompoundNBT nbt;

    public SpatialEntry(SpatialType type, double weight, @Nullable CompoundNBT nbt) {
        this.type = type;
        this.weight = weight;
        this.nbt = nbt;
    }

    public SpatialType getType() {
        return type;
    }

    public double getWeight() {
        return weight;
    }

    public CompoundNBT getNBT() {
        return nbt;
    }
}
