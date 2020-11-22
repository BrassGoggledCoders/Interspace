package xyz.brassgoggledcoders.interspace.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SingleCapabilityProvider<CAP extends INBTSerializable<CompoundNBT>> implements ICapabilitySerializable<CompoundNBT> {
    private final Capability<CAP> providedCapability;
    private final CAP providedInstance;
    private final LazyOptional<CAP> lazyInstance;

    public SingleCapabilityProvider(Capability<CAP> providedCapability, CAP providedInstance) {
        this.providedCapability = providedCapability;
        this.providedInstance = providedInstance;
        this.lazyInstance = LazyOptional.of(this::getProvidedInstance);
    }

    @Nonnull
    public CAP getProvidedInstance() {
        return providedInstance;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        return providedCapability == capability ? this.lazyInstance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return providedInstance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        providedInstance.deserializeNBT(nbt);
    }

    public void invalidate() {
        this.lazyInstance.invalidate();
    }
}
