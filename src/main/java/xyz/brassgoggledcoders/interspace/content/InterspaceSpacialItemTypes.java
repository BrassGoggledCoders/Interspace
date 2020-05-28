package xyz.brassgoggledcoders.interspace.content;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.InterspaceRegistries;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItemType;
import xyz.brassgoggledcoders.interspace.spacial.item.ItemStackSpacialItemType;

public class InterspaceSpacialItemTypes {

    private static final DeferredRegister<SpacialItemType<?>> SPACIAL_ITEM_TYPES = new DeferredRegister<>(
            InterspaceRegistries.SPACIAL_ITEM_TYPES, Interspace.ID);

    public static final RegistryObject<ItemStackSpacialItemType> ITEM_STACK = SPACIAL_ITEM_TYPES.register(
            "item_stack", ItemStackSpacialItemType::new);

    public static void register(IEventBus modBus) {
        SPACIAL_ITEM_TYPES.register(modBus);
    }
}
