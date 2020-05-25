package xyz.brassgoggledcoders.interspace.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspace;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItemType;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialType;

public class InterspaceAPI {
    @CapabilityInject(IInterspace.class)
    public static final Capability<IInterspace> INTERSPACE = null;

    public static IForgeRegistry<SpacialType> SPACIAL_TYPE = RegistryManager.ACTIVE.getRegistry(SpacialType.class);
    public static IForgeRegistry<SpacialItemType<?>> SPACIAL_ITEM_TYPE = RegistryManager.ACTIVE.getRegistry(SpacialItemType.class);
}
