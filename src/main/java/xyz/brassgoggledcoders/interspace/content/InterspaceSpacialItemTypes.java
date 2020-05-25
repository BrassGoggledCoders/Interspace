package xyz.brassgoggledcoders.interspace.content;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItemType;
import xyz.brassgoggledcoders.interspace.registration.LazyForgeRegistry;
import xyz.brassgoggledcoders.interspace.spacial.item.ItemStackSpacialItemType;

public class InterspaceSpacialItemTypes {
    private static final DeferredRegister<SpacialItemType<?>> SPACIAL_ITEM_TYPE = new DeferredRegister<>(
            LazyForgeRegistry.<SpacialItemType<?>>of(Interspace.rl("spacial_item_type")), Interspace.ID);

    public static final RegistryObject<ItemStackSpacialItemType> ITEM_STACK = SPACIAL_ITEM_TYPE.register(
            "item_stack", ItemStackSpacialItemType::new);

    public static void register(IEventBus modBus) {
        SPACIAL_ITEM_TYPE.register(modBus);
    }
}
