package xyz.brassgoggledcoders.interspace.nbt;

import net.minecraft.nbt.CompoundNBT;

public class CompoundNBTBuilder {
    private final CompoundNBT compoundNBT;

    public CompoundNBTBuilder(CompoundNBT compoundNBT) {
        this.compoundNBT = compoundNBT;
    }

    public CompoundNBTBuilder withString(String name, String value) {
        this.compoundNBT.putString(name, value);
        return this;
    }

    public CompoundNBT build() {
        return compoundNBT;
    }

    public static CompoundNBTBuilder create() {
        return new CompoundNBTBuilder(new CompoundNBT());
    }
}
