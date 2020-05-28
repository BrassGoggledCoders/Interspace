package xyz.brassgoggledcoders.interspace;

import net.minecraftforge.registries.IForgeRegistry;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItemType;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialType;
import xyz.brassgoggledcoders.interspace.registration.LazyForgeRegistry;

public class InterspaceRegistries {
    public static final IForgeRegistry<SpacialItemType<?>> SPACIAL_ITEM_TYPES = LazyForgeRegistry.of(
            Interspace.rl("spacial_item_type"));

    public static final IForgeRegistry<SpacialType> SPACIAL_TYPES = LazyForgeRegistry.of(
            Interspace.rl("spacial_type"));

}
