package xyz.brassgoggledcoders.interspace.content;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.InterspaceRegistries;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItemType;
import xyz.brassgoggledcoders.interspace.spatial.item.ItemStackSpatialItemType;

public class InterspaceSpatialItemTypes {

    private static final DeferredRegister<SpatialItemType<?>> SPACIAL_ITEM_TYPES = new DeferredRegister<>(
            InterspaceRegistries.SPACIAL_ITEM_TYPES, Interspace.ID);

    public static final RegistryObject<ItemStackSpatialItemType> ITEM_STACK = SPACIAL_ITEM_TYPES.register(
            "item_stack", ItemStackSpatialItemType::new);

    public static void register(IEventBus modBus) {
        SPACIAL_ITEM_TYPES.register(modBus);
    }
}
