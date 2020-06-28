package xyz.brassgoggledcoders.interspace.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.content.InterspaceTileEntities;

import javax.annotation.Nullable;

public class ObeliskConnectedTileEntity extends PassThroughSpatialTileEntity<ISpatial> {
    private BlockPos controllerPosition;

    public ObeliskConnectedTileEntity() {
        super(InterspaceTileEntities.OBELISK_CONNECTED.get());
    }

    public void setControllerPosition(BlockPos blockPos) {
        this.controllerPosition = blockPos;
        this.invalidate();
    }

    @Nullable
    private ObeliskControllerTileEntity getController() {
        if (controllerPosition != null && this.getWorld().isAreaLoaded(controllerPosition, 1)) {
            TileEntity tileEntity = this.getWorld().getTileEntity(controllerPosition);
            if (tileEntity instanceof ObeliskControllerTileEntity) {
                return (ObeliskControllerTileEntity) tileEntity;
            }
        }
        return null;
    }

    @Override
    protected LazyOptional<ISpatial> getCapabilityForPassThrough() {
        ObeliskControllerTileEntity controller = this.getController();
        if (controller != null) {
            return controller.getCapability(InterspaceAPI.SPATIAL);
        } else {
            return LazyOptional.empty();
        }
    }
}
