package xyz.brassgoggledcoders.interspace.api;

import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceManager;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspacePostOffice;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class InterspaceAPI {
    public static final UUID INTERSPACE_UUID = UUID.fromString("83801A9C-C70B-4B91-B615-F39461FB6295");

    private static IInterspaceManager interspaceManager;
    private static IInterspacePostOffice interspacePostOffice;


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
        return Objects.requireNonNull(interspacePostOffice, "Called for InterspacePost before it was Ready");
    }
}
