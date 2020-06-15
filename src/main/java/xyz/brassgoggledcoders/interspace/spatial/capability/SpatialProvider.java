package xyz.brassgoggledcoders.interspace.spatial.capability;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpatialProvider<CAP> implements ICapabilityProvider {
    private final Capability<CAP> capability;
    private final LazyOptional<CAP> lazyInterspace;

    public SpatialProvider(Capability<CAP> capability, LazyOptional<CAP> lazyInterspace) {
        this.capability = capability;
        this.lazyInterspace = lazyInterspace;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return capability.orEmpty(cap, lazyInterspace);
    }
}
