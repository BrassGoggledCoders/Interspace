package xyz.brassgoggledcoders.interspace.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.query.SpatialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;
import xyz.brassgoggledcoders.interspace.block.QuerySlateBlock;
import xyz.brassgoggledcoders.interspace.content.InterspaceTileEntities;

import java.util.Collection;

public class QuerySlateTileEntity extends TileEntity implements ITickableTileEntity {
    private LazyOptional<ISpatial> spatialLazyOptional;

    private Transaction<Collection<SpatialItem>> currentTransaction;

    public QuerySlateTileEntity() {
        this(InterspaceTileEntities.QUERY_SLATE.get());
    }

    public QuerySlateTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public LazyOptional<ISpatial> getSpatial() {
        if (this.spatialLazyOptional == null) {
            Direction facing = this.getBlockState().get(QuerySlateBlock.FACING);
            TileEntity tileEntity = this.getWorld().getTileEntity(this.getPos().offset(facing.getOpposite()));
            if (tileEntity != null) {
                this.spatialLazyOptional = tileEntity.getCapability(InterspaceAPI.SPATIAL, facing);
                this.spatialLazyOptional.addListener(this::handleInvalidation);
            } else {
                this.spatialLazyOptional = LazyOptional.empty();
            }
        }
        return this.spatialLazyOptional;
    }

    private void handleInvalidation(LazyOptional<ISpatial> lazyOptional) {
        this.spatialLazyOptional = null;
    }

    @Override
    public void tick() {
        if (currentTransaction == null || currentTransaction.isDone()) {
            currentTransaction = this.getSpatial().map(lazy -> lazy.query(SpatialQueryBuilder.create()))
                    .orElseGet(Transaction::ofEmpty);
        }
    }
}
