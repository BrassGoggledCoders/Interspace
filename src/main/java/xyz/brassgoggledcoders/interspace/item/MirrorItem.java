package xyz.brassgoggledcoders.interspace.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialInstance;
import xyz.brassgoggledcoders.interspace.block.ObeliskCoreBlock;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;
import xyz.brassgoggledcoders.interspace.content.tag.InterspaceBlockTags;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class MirrorItem extends Item {
    public MirrorItem() {
        this(new Item.Properties()
                .maxStackSize(1)
                .group(Interspace.ITEM_GROUP)
        );
    }

    public MirrorItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        IChunk chunk = world.getChunk(player.getPosition());
        if (chunk instanceof ICapabilityProvider) {
            return ((ICapabilityProvider) chunk).getCapability(InterspaceAPI.INTERSPACE_CHUNK)
                    .map(interspace -> {
                        SpatialInstance spatialInstance = interspace.getSpacialInstance();
                        if (!world.isRemote()) {
                            player.sendStatusMessage(new TranslationTextComponent("text.interspace.gaze",
                                            spatialInstance.getDisplayName()), false);
                        }
                        return ActionResult.resultSuccess(itemStack);
                    })
                    .orElseGet(() -> ActionResult.resultPass(itemStack));
        } else {
            return ActionResult.resultPass(itemStack);
        }
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(@Nonnull ItemUseContext context) {
        World world = context.getWorld();
        BlockPos hitPos = context.getPos();
        BlockState hitBlockState = world.getBlockState(hitPos);
        if (!world.isRemote() && hitBlockState.getBlock().isIn(InterspaceBlockTags.NAFASI)) {
            BlockPos corePos = hitPos.offset(context.getFace().getOpposite());
            BlockState coreBlockState = world.getBlockState(corePos);
            if (coreBlockState.getBlock() == InterspaceBlocks.NAFASI.getPrimary()) {
                ObeliskCoreBlock obeliskCoreBlock = InterspaceBlocks.OBELISK_CORE.getPrimary();
                if (ObeliskCoreBlock.isValid(world, corePos)) {
                    world.setBlockState(corePos, obeliskCoreBlock.getDefaultState(), Constants.BlockFlags.DEFAULT);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return super.onItemUse(context);
    }
}
