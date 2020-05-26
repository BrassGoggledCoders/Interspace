package xyz.brassgoggledcoders.interspace.api;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootParameterSet;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspace;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspaceClient;

import java.util.Objects;

public class InterspaceAPI {
    @CapabilityInject(IInterspace.class)
    public static Capability<IInterspace> INTERSPACE;

    private static IInterspaceClient interspaceClient = null;

    public static IInterspaceClient getInterspaceClient() {
        return Objects.requireNonNull(interspaceClient, "Called for Client before it exists");
    }

    public static void setInterspaceClient(IInterspaceClient clientSupplier) {
        InterspaceAPI.interspaceClient = clientSupplier;
    }
}
