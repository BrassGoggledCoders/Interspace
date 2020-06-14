package xyz.brassgoggledcoders.interspace.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import xyz.brassgoggledcoders.interspace.api.spacial.capability.IInterspaceChunk;
import xyz.brassgoggledcoders.interspace.api.spacial.capability.IInterspaceWorld;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspaceClient;
import xyz.brassgoggledcoders.interspace.api.spacial.entry.ISpacialEntryManager;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Supplier;

public class InterspaceAPI {
    @CapabilityInject(IInterspaceWorld.class)
    public static Capability<IInterspaceWorld> INTERSPACE_WORLD;
    @CapabilityInject(IInterspaceChunk.class)
    public static Capability<IInterspaceChunk> INTERSPACE_CHUNK;

    private static Supplier<IInterspaceClient> interspaceClient = () -> null;
    private static ISpacialEntryManager spacialEntryManager = null;

    @Nonnull
    public static IInterspaceClient getInterspaceClient() {
        return Objects.requireNonNull(interspaceClient.get(), "Called for Client before it exists");
    }

    public static void setInterspaceClientSupplier(Supplier<IInterspaceClient> clientSupplier) {
        InterspaceAPI.interspaceClient = clientSupplier;
    }

    public static ISpacialEntryManager getSpacialEntryManager() {
        return Objects.requireNonNull(spacialEntryManager, "Called for Spacial Entry Manager before it exists");
    }

    public static void setSpacialEntryManager(ISpacialEntryManager spacialEntryManager) {
        InterspaceAPI.spacialEntryManager = spacialEntryManager;
    }
}
