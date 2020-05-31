package xyz.brassgoggledcoders.interspace.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.interspace.api.functional.TriFunction;

import javax.annotation.Nonnull;

public class QuerySlateItem<T extends HangingEntity> extends Item {
    private final TriFunction<World, BlockPos, Direction, T> entitySupplier;

    public QuerySlateItem(TriFunction<World, BlockPos, Direction, T> entitySupplier, Properties properties) {
        super(properties);
        this.entitySupplier = entitySupplier;
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(ItemUseContext context) {
        BlockPos hitPos = context.getPos();
        Direction direction = context.getFace();
        BlockPos hitBlockPos = hitPos.offset(direction);
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getItem();
        if (playerEntity != null && !this.canPlace(playerEntity, direction, itemStack, hitBlockPos)) {
            return ActionResultType.FAIL;
        } else {
            World world = context.getWorld();
            T entity = entitySupplier.apply(world, hitBlockPos, direction);

            CompoundNBT compoundnbt = itemStack.getTag();
            if (compoundnbt != null) {
                EntityType.applyItemNBT(world, playerEntity, entity, compoundnbt);
            }

            if (entity.onValidSurface()) {
                if (!world.isRemote) {
                    entity.playPlaceSound();
                    world.addEntity(entity);
                }

                itemStack.shrink(1);
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.CONSUME;
            }
        }
    }

    protected boolean canPlace(PlayerEntity playerIn, Direction directionIn, ItemStack itemStackIn, BlockPos posIn) {
        return !directionIn.getAxis().isVertical() && playerIn.canPlayerEdit(posIn, directionIn, itemStackIn);
    }
}
