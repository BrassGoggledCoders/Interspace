package xyz.brassgoggledcoders.interspace.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.spatial.capability.EmptySpatial;
import xyz.brassgoggledcoders.interspace.spatial.capability.PassThroughSpatial;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class PassThroughSpatialTileEntity<T extends ISpatial> extends TileEntity {
    private LazyOptional<ISpatial> spatialLazy = LazyOptional.of(this::supplyLazy);

    public PassThroughSpatialTileEntity(TileEntityType<? extends PassThroughSpatialTileEntity> tileEntityType) {
        super(tileEntityType);
    }

    @Nonnull
    @Override
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        if (cap == InterspaceAPI.SPATIAL) {
            return spatialLazy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Nonnull
    private ISpatial supplyLazy() {
        LazyOptional<T> controllerSpatial = this.getCapabilityForPassThrough();
        if (controllerSpatial.isPresent()) {
            controllerSpatial.addListener(this::handleInvalidation);
            return controllerSpatial.<ISpatial>map(PassThroughSpatial::new)
                    .orElseGet(EmptySpatial::new);
        } else {
            return new EmptySpatial();
        }
    }

    protected void invalidate() {
        this.spatialLazy.invalidate();
        this.spatialLazy = LazyOptional.of(this::supplyLazy);
    }

    protected void handleInvalidation(LazyOptional<T> passedThroughLazy) {
        this.invalidate();
    }

    protected abstract LazyOptional<T> getCapabilityForPassThrough();

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        this.spatialLazy.invalidate();
    }
}
