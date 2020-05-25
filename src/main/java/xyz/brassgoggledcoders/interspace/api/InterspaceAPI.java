package xyz.brassgoggledcoders.interspace.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspace;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItemType;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialType;

import java.util.Objects;

public class InterspaceAPI {
    @CapabilityInject(IInterspace.class)
    public static Capability<IInterspace> INTERSPACE;

    public static final IForgeRegistry<SpacialType> SPACIAL_TYPE = RegistryManager.ACTIVE.getRegistry(SpacialType.class);
    public static final IForgeRegistry<SpacialItemType<?>> SPACIAL_ITEM_TYPE = RegistryManager.ACTIVE.getRegistry(SpacialItemType.class);

    private static IInterspaceClient interspaceClient = null;

    public static IInterspaceClient getInterspaceClient() {
        return Objects.requireNonNull(interspaceClient, "Called for Client before it exists");
    }

    public static void setInterspaceClient(IInterspaceClient clientSupplier) {
        InterspaceAPI.interspaceClient = clientSupplier;
    }
}
