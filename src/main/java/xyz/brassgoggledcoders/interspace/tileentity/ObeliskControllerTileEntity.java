package xyz.brassgoggledcoders.interspace.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialChunk;
import xyz.brassgoggledcoders.interspace.content.InterspaceTileEntities;

public class ObeliskControllerTileEntity extends PassThroughSpatialTileEntity<ISpatialChunk> implements ITickableTileEntity {
    public ObeliskControllerTileEntity() {
        super(InterspaceTileEntities.OBELISK_CONTROLLER.get());
    }

    @Override
    protected LazyOptional<ISpatialChunk> getCapabilityForPassThrough() {
        return this.getWorld().getChunkAt(this.pos).getCapability(InterspaceAPI.SPATIAL_CHUNK);
    }

    @Override
    public void tick() {

    }
}
