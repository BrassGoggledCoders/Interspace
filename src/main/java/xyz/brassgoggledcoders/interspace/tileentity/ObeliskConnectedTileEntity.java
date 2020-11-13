package xyz.brassgoggledcoders.interspace.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.interspace.content.InterspaceTileEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ObeliskConnectedTileEntity extends TileEntity {
    private BlockPos controllerPosition;

    public ObeliskConnectedTileEntity() {
        super(InterspaceTileEntities.OBELISK_CONNECTED.get());
    }

    public void setControllerPosition(BlockPos blockPos) {
        this.controllerPosition = blockPos;
    }

    @Nullable
    private ObeliskControllerTileEntity getController() {
        if (controllerPosition != null && this.getWorld() != null && this.getWorld().isAreaLoaded(controllerPosition, 1)) {
            TileEntity tileEntity = this.getWorld().getTileEntity(controllerPosition);
            if (tileEntity instanceof ObeliskControllerTileEntity) {
                return (ObeliskControllerTileEntity) tileEntity;
            }
        }
        return null;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        ObeliskControllerTileEntity controllerTileEntity = this.getController();
        if (controllerTileEntity != null) {
            LazyOptional<T> lazyOptional = controllerTileEntity.getCapability(cap, side);
            if (lazyOptional.isPresent()) {
                return lazyOptional;
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        CompoundNBT superCompound = super.write(compound);
        if (controllerPosition != null) {
            superCompound.putLong("controllerPos", controllerPosition.toLong());
        }
        return superCompound;
    }

    @Override
    public void read(@Nonnull BlockState blockState, @Nonnull CompoundNBT compound) {
        super.read(blockState, compound);
        if (compound.contains("controllerPos")) {
            this.setControllerPosition(BlockPos.fromLong(compound.getLong("controllerPos")));
        }
    }
}
