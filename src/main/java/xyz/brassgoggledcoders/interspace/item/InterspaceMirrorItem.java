package xyz.brassgoggledcoders.interspace.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.InterspaceCapabilities;
import xyz.brassgoggledcoders.interspace.task.interspace.AnnounceInterspaceTask;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class InterspaceMirrorItem extends Item {
    public InterspaceMirrorItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote()) {
            InterspaceAPI.getManager().submitTask(new AnnounceInterspaceTask(World.OVERWORLD.getLocation(),
                    new ChunkPos(player.getPosition()), player.getUniqueID()));
        }
        return super.onItemRightClick(world, player, hand);
    }
}
