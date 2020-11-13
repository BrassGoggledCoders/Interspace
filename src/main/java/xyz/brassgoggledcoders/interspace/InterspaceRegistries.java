package xyz.brassgoggledcoders.interspace;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItemType;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;

public class InterspaceRegistries {
    public static final IForgeRegistry<SpatialItemType<?>> SPACIAL_ITEM_TYPES = RegistryManager.ACTIVE.getRegistry(
            Interspace.rl("spacial_item_types"));

    public static final IForgeRegistry<SpatialType> SPACIAL_TYPES = RegistryManager.ACTIVE.getRegistry(
            Interspace.rl("spacial_type"));

}
