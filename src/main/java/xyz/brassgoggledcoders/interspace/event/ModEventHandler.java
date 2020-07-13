package xyz.brassgoggledcoders.interspace.event;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItemType;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;

@EventBusSubscriber(modid = Interspace.ID, bus = Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void newRegistry(RegistryEvent.NewRegistry newRegistry) {
        makeRegistry("spacial_type", SpatialType.class);
        makeRegistry("spacial_item_type", SpatialItemType.class);
    }

    private static <T extends IForgeRegistryEntry<T>> void makeRegistry(String name, Class<T> type) {
        new RegistryBuilder<T>()
                .setName(Interspace.rl(name))
                .setType(type)
                .create();
    }

    @SubscribeEvent
    public static void handleIMC(InterModProcessEvent event) {
        event.getIMCStream("obelisk"::equalsIgnoreCase)
                .forEach(message ->
                        InterspaceAPI.registerObeliskHandler(message.<Capability<?>>getMessageSupplier().get())
                );
    }

    @SubscribeEvent
    public static void queueIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(Interspace.ID, "obelisk", () -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    }
}
