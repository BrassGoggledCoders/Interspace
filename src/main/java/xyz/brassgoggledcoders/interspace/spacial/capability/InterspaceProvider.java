package xyz.brassgoggledcoders.interspace.spacial.capability;

import net.minecraft.util.Direction;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullLazy;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spacial.capability.IInterspaceWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InterspaceProvider<CAP> implements ICapabilityProvider {
    private final Capability<CAP> capability;
    private final LazyOptional<CAP> lazyInterspace;

    public InterspaceProvider(Capability<CAP> capability, LazyOptional<CAP> lazyInterspace) {
        this.capability = capability;
        this.lazyInterspace = lazyInterspace;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return capability.orEmpty(cap, lazyInterspace);
    }
}
