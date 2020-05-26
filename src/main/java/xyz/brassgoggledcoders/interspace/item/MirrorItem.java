package xyz.brassgoggledcoders.interspace.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;

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

        return world.getCapability(InterspaceAPI.INTERSPACE)
                .map(interspace -> {
                    SpacialInstance spacialInstance = interspace.getSpacialInstance(new ChunkPos(player.getPosition()));
                    ITextComponent instanceText;
                    if (spacialInstance == null) {
                        instanceText = new TranslationTextComponent("text.interspace.nothing");
                    } else {
                        instanceText = spacialInstance.getType().getDisplayName();
                    }
                    if (!world.isRemote()) {
                        player.sendStatusMessage(new TranslationTextComponent("text.interspace.gaze", instanceText),
                                false);
                    }
                    return ActionResult.resultSuccess(itemStack);
                })
                .orElseGet(() -> ActionResult.resultPass(itemStack));
    }
}
