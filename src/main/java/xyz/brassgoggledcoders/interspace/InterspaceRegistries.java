package xyz.brassgoggledcoders.interspace;

import net.minecraftforge.registries.IForgeRegistry;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItemType;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;
import xyz.brassgoggledcoders.interspace.registration.LazyForgeRegistry;

public class InterspaceRegistries {
    public static final IForgeRegistry<SpatialItemType<?>> SPACIAL_ITEM_TYPES = LazyForgeRegistry.of(
            Interspace.rl("spacial_item_type"));

    public static final IForgeRegistry<SpatialType> SPACIAL_TYPES = LazyForgeRegistry.of(
            Interspace.rl("spacial_type"));

}
