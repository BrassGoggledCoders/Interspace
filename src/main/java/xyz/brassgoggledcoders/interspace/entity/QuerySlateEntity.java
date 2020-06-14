package xyz.brassgoggledcoders.interspace.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spacial.capability.IInterspaceChunk;
import xyz.brassgoggledcoders.interspace.api.spacial.query.SpacialQueryBuilder;
import xyz.brassgoggledcoders.interspace.block.ObeliskCoreBlock;
import xyz.brassgoggledcoders.interspace.content.InterspaceEntities;
import xyz.brassgoggledcoders.interspace.content.InterspaceItems;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpacialItemTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class QuerySlateEntity extends ItemFrameEntity {
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
            BlockPos corePos = this.hangingPosition.offset(this.facingDirection.getOpposite(), 2);
            BlockState coreBlockState = this.getEntityWorld().getBlockState(corePos);
            if (coreBlockState.getBlock() instanceof ObeliskCoreBlock) {
                if (coreBlockState.get(BlockStateProperties.ATTACHED)) {
                    this.getEntityWorld().getCapability(InterspaceAPI.INTERSPACE_CHUNK)
                            .ifPresent(this::handleInterspace);
                }
            }
        }
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void handleInterspace(IInterspaceChunk interspace) {
        interspace.retrieve(SpacialQueryBuilder.create()
                .build())
                .getResult()
                .thenAccept(spacialItems -> spacialItems.stream()
                        .filter(spacialItem -> spacialItem.getType() == InterspaceSpacialItemTypes.ITEM_STACK.get())
                        .map(InterspaceSpacialItemTypes.ITEM_STACK.get()::fromSpacialItem)
                        .forEach(this::entityDropItem));

    }
}
