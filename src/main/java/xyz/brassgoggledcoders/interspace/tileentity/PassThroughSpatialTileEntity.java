package xyz.brassgoggledcoders.interspace.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.spatial.capability.PassThroughSpatial;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class PassThroughSpatialTileEntity<T extends ISpatial> extends TileEntity {
    private LazyOptional<ISpatial> spatialLazy = null;

    public PassThroughSpatialTileEntity(TileEntityType<? extends PassThroughSpatialTileEntity> tileEntityType) {
        super(tileEntityType);
    }

    @Nonnull
    @Override
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        if (cap == InterspaceAPI.SPATIAL) {
            return this.getSpatial().cast();
        }
        return super.getCapability(cap, side);
    }

    @Nonnull
    private LazyOptional<ISpatial> supplyLazy() {
        return this.getCapabilityForPassThrough()
                .map(PassThroughSpatial::new);
    }

    protected void invalidate() {
        if (this.spatialLazy != null && this.spatialLazy.isPresent()) {
            this.spatialLazy.invalidate();
        }
        this.spatialLazy = null;
    }

    protected void handleInvalidation(LazyOptional<ISpatial> passedThroughLazy) {
        this.invalidate();
    }

    protected abstract LazyOptional<T> getCapabilityForPassThrough();

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        this.spatialLazy.invalidate();
    }

    protected LazyOptional<ISpatial> getSpatial() {
        if (this.spatialLazy == null) {
            this.spatialLazy = this.supplyLazy();
            if (this.spatialLazy.isPresent()) {
                this.spatialLazy.addListener(this::handleInvalidation);
            }
        }
        return this.spatialLazy;
    }
}
