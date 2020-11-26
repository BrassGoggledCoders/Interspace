package xyz.brassgoggledcoders.interspace.event;

import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.content.InterspaceFeatures;

@EventBusSubscriber(modid = InterspaceMod.ID, bus = Bus.MOD)
public class ModEventHandler {

    @SubscribeEvent
    public static void onFeatureRegistry(RegistryEvent.Register<Feature<?>> event) {
        InterspaceFeatures.registerFeatures();
    }
}
