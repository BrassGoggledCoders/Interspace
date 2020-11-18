package xyz.brassgoggledcoders.interspace.sql;

import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.interspace.InterspaceMod;

@EventBusSubscriber(modid = InterspaceMod.ID, bus = Bus.FORGE)
public class InterspaceSQL {

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load worldLoadEvent) {
        if (!worldLoadEvent.getWorld().isRemote() && worldLoadEvent.getWorld() instanceof World) {
            World world = (World) worldLoadEvent.getWorld();

        }
    }
}
