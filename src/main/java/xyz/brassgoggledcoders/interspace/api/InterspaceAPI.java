package xyz.brassgoggledcoders.interspace.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialChunk;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialWorld;
import xyz.brassgoggledcoders.interspace.api.spatial.ISpatialClient;
import xyz.brassgoggledcoders.interspace.api.spatial.entry.ISpatialEntryManager;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Supplier;

public class InterspaceAPI {
    @CapabilityInject(ISpatialWorld.class)
    public static Capability<ISpatialWorld> SPATIAL_WORLD;
    @CapabilityInject(ISpatialChunk.class)
    public static Capability<ISpatialChunk> SPATIAL_CHUNK;
    @CapabilityInject(ISpatial.class)
    public static Capability<ISpatial> SPATIAL;

    private static Supplier<ISpatialClient> interspaceClient = () -> null;
    private static ISpatialEntryManager spacialEntryManager = null;

    @Nonnull
    public static ISpatialClient getInterspaceClient() {
        return Objects.requireNonNull(interspaceClient.get(), "Called for Client before it exists");
    }

    public static void setInterspaceClientSupplier(Supplier<ISpatialClient> clientSupplier) {
        InterspaceAPI.interspaceClient = clientSupplier;
    }

    public static ISpatialEntryManager getSpacialEntryManager() {
        return Objects.requireNonNull(spacialEntryManager, "Called for Spacial Entry Manager before it exists");
    }

    public static void setSpacialEntryManager(ISpatialEntryManager spacialEntryManager) {
        InterspaceAPI.spacialEntryManager = spacialEntryManager;
    }
}
