package xyz.brassgoggledcoders.interspace.spacial.capability;

import net.minecraft.util.Direction;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullLazy;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspace;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InterspaceWorldProvider implements ICapabilityProvider {
    private final LazyOptional<IInterspace> lazyInterspace;

    public InterspaceWorldProvider(IWorld world) {
        this.lazyInterspace = LazyOptional.of(NonNullLazy.of(() -> new InterspaceWorld(world)));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return InterspaceAPI.INTERSPACE.orEmpty(cap, lazyInterspace);
    }
}
