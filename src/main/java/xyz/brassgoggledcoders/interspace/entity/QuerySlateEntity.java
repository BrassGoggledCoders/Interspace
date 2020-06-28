package xyz.brassgoggledcoders.interspace.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialChunk;
import xyz.brassgoggledcoders.interspace.api.spatial.query.SpatialQueryBuilder;
import xyz.brassgoggledcoders.interspace.block.ObeliskCoreBlock;
import xyz.brassgoggledcoders.interspace.content.InterspaceEntities;
import xyz.brassgoggledcoders.interspace.content.InterspaceItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class QuerySlateEntity extends ItemFrameEntity {
    private LazyOptional<ISpatial> hangCap;
    private CompletableFuture<Void> currentPull;

    public QuerySlateEntity(EntityType<? extends QuerySlateEntity> type, World world) {
        super(type, world);
    }

    public QuerySlateEntity(World world, BlockPos blockPos, Direction direction) {
        this(InterspaceEntities.QUERY_SLATE.get(), world);
        this.hangingPosition = blockPos;
        this.updateFacingWithBoundingBox(direction);
    }

    @Override
    @Nullable
    public ItemEntity entityDropItem(@Nonnull IItemProvider item) {
        if (item.asItem() == Items.ITEM_FRAME) {
            return this.entityDropItem(InterspaceItems.QUERY_SLATE.get(), 0);
        } else {
            return this.entityDropItem(item, 0);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (currentPull == null || currentPull.isDone()) {
            if (hangCap == null) {
                hangCap = this.getHangingCap();
            }
            hangCap.ifPresent(this::handleInterspace);
        }
    }

    private LazyOptional<ISpatial> getHangingCap() {
        TileEntity tileEntity = this.getEntityWorld()
                .getTileEntity(this.hangingPosition.offset(this.facingDirection.getOpposite()));
        if (tileEntity != null) {
            return tileEntity.getCapability(InterspaceAPI.SPATIAL, this.facingDirection);
        } else {
            return LazyOptional.empty();
        }
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void handleInterspace(ISpatial interspace) {
        currentPull = interspace.query(SpatialQueryBuilder.create())
                .getResult()
                .thenAccept(spatialItems -> spatialItems
                        .forEach(spatialItem -> {
                            Interspace.LOGGER.info(spatialItem.toString());
                        }));

    }
}
