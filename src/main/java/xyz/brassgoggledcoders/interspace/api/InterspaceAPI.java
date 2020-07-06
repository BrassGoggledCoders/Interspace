package xyz.brassgoggledcoders.interspace.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import xyz.brassgoggledcoders.interspace.api.spatial.ISpatialClient;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialChunk;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialWorld;
import xyz.brassgoggledcoders.interspace.api.spatial.entry.ISpatialEntryManager;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntSupplier;
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
    private final static Set<Capability<?>> OBELISK_CAPABILITIES = Sets.newHashSet();

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

    public static void registerObeliskHandler(Capability<?> obeliskCapability) {
        InterspaceAPI.OBELISK_CAPABILITIES.add(obeliskCapability);
    }

    public static Collection<Capability<?>> getObeliskHandlers() {
        return InterspaceAPI.OBELISK_CAPABILITIES;
    }
}
