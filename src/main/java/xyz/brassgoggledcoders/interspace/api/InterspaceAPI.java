package xyz.brassgoggledcoders.interspace.api;

import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceCacheManager;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceManager;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspacePostOffice;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceVolumeManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class InterspaceAPI {
    public static final UUID INTERSPACE_UUID = UUID.fromString("83801A9C-C70B-4B91-B615-F39461FB6295");

    private static IInterspaceManager interspaceManager;
    private static IInterspacePostOffice interspacePostOffice;
    private static IInterspaceVolumeManager interspaceVolumeManager;
    private static IInterspaceCacheManager interspaceCacheManager;

    public static void setManager(@Nullable IInterspaceManager interspaceManager) {
        InterspaceAPI.interspaceManager = interspaceManager;
    }

    @Nonnull
    public static IInterspaceManager getManager() {
        return Objects.requireNonNull(interspaceManager, "Called For InterspaceManager before it was Ready");
    }

    public static void setPostOffice(IInterspacePostOffice interspacePostOffice) {
        InterspaceAPI.interspacePostOffice = interspacePostOffice;
    }

    @Nonnull
    public static IInterspacePostOffice getPostOffice() {
        return Objects.requireNonNull(interspacePostOffice, "Called for Interspace Post Office before it was Ready");
    }

    public static void setVolumeManager(IInterspaceVolumeManager interspaceVolumeManager) {
        InterspaceAPI.interspaceVolumeManager = interspaceVolumeManager;
    }

    @Nonnull
    public static IInterspaceVolumeManager getVolumeManager() {
        return Objects.requireNonNull(interspaceVolumeManager, "Called for Interspace Volume Manager before it was Ready");
    }

    public static void setCacheManager(IInterspaceCacheManager interspaceCacheManager) {
        InterspaceAPI.interspaceCacheManager = interspaceCacheManager;
    }

    @Nonnull
    public static IInterspaceCacheManager getCacheManager() {
        return Objects.requireNonNull(interspaceCacheManager, "Called for Interspace Cache Manager before it was Ready");
    }

    public static ResourceLocation rl(String location) {
        return new ResourceLocation("interspace", location);
    }
}
