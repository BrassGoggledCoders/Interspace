package xyz.brassgoggledcoders.interspace.api;

import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceManager;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspacePostOffice;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class InterspaceAPI {
    private static IInterspaceManager interspaceManager;
    private static IInterspacePostOffice interspacePost;

    public static void setManager(@Nullable IInterspaceManager interspaceManager) {
        InterspaceAPI.interspaceManager = interspaceManager;
    }

    @Nonnull
    public static IInterspaceManager getManager() {
        return Objects.requireNonNull(interspaceManager, "Called For InterspaceManager before it was Ready");
    }

    public static void setPost(IInterspacePostOffice interspacePost) {
        InterspaceAPI.interspacePost = interspacePost;
    }

    @Nonnull
    public static IInterspacePostOffice getPost() {
        return Objects.requireNonNull(interspacePost, "Called for InterspacePost before it was Ready");
    }
}
