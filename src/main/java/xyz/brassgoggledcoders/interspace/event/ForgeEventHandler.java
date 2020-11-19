package xyz.brassgoggledcoders.interspace.event;

import com.google.common.collect.Sets;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.source.WorldSource;
import xyz.brassgoggledcoders.interspace.manager.InterspaceManager;
import xyz.brassgoggledcoders.interspace.task.interspace.SetupInterspaceTask;

import java.util.Set;

@EventBusSubscriber(modid = InterspaceMod.ID, bus = Bus.FORGE)
public class ForgeEventHandler {
    private static final Set<RegistryKey<World>> HANDLED = Sets.newHashSet();

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load worldLoadEvent) {
        if (InterspaceManager.isReady() && !worldLoadEvent.getWorld().isRemote() &&
                worldLoadEvent.getWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) worldLoadEvent.getWorld();
            if (HANDLED.add(world.getDimensionKey())) {
                InterspaceAPI.getManager().submitTask(new SetupInterspaceTask(
                        new WorldSource(world.getDimensionKey())));
            }
        }
    }

    @SubscribeEvent
    public static void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        HANDLED.clear();
    }

    @SubscribeEvent
    public static void onServerStopped(FMLServerStoppingEvent event) {
        HANDLED.clear();
    }
}
