package xyz.brassgoggledcoders.interspace.api;

import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceManager;

import javax.annotation.Nonnull;
import java.util.Objects;

public class InterspaceAPI {
    private static IInterspaceManager interspaceManager;

    public static void setManager(@Nonnull IInterspaceManager interspaceManager) {
        InterspaceAPI.interspaceManager = interspaceManager;
    }

    @Nonnull
    public static IInterspaceManager getManager() {
        return Objects.requireNonNull(interspaceManager, "Called For InterspaceManager before it was Ready");
    }
}
