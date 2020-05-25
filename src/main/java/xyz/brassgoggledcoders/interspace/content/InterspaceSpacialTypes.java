package xyz.brassgoggledcoders.interspace.content;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItemType;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialType;
import xyz.brassgoggledcoders.interspace.registration.LazyForgeRegistry;
import xyz.brassgoggledcoders.interspace.spacial.item.ItemStackSpacialItemType;

public class InterspaceSpacialTypes {
    private static final DeferredRegister<SpacialType> SPACIAL_TYPE = new DeferredRegister<>(
            LazyForgeRegistry.<SpacialType>of(Interspace.rl("spacial_type")), Interspace.ID);

    public static final RegistryObject<SpacialType> EMPTY = SPACIAL_TYPE.register("empty", () -> new SpacialType(SpacialInstance::new));

    public static void register(IEventBus modBus) {
        SPACIAL_TYPE.register(modBus);
    }
}
