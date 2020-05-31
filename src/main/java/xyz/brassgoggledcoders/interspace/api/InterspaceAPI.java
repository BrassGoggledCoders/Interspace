package xyz.brassgoggledcoders.interspace.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspace;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspaceClient;
import xyz.brassgoggledcoders.interspace.api.spacial.entry.ISpacialEntryManager;

import java.util.Objects;

public class InterspaceAPI {
    @CapabilityInject(IInterspace.class)
    public static Capability<IInterspace> INTERSPACE;

    private static IInterspaceClient interspaceClient = null;
    private static ISpacialEntryManager spacialEntryManager = null;

    public static IInterspaceClient getInterspaceClient() {
        return Objects.requireNonNull(interspaceClient, "Called for Client before it exists");
    }

    public static void setInterspaceClient(IInterspaceClient clientSupplier) {
        InterspaceAPI.interspaceClient = clientSupplier;
    }

    public static ISpacialEntryManager getSpacialEntryManager() {
        return Objects.requireNonNull(spacialEntryManager, "Called for Spacial Entry Manager before it exists");
    }

    public static void setSpacialEntryManager(ISpacialEntryManager spacialEntryManager) {
        InterspaceAPI.spacialEntryManager = spacialEntryManager;
    }
}
