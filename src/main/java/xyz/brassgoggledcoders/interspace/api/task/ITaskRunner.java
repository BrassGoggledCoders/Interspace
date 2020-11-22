package xyz.brassgoggledcoders.interspace.api.task;

import org.apache.logging.log4j.Level;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ITaskRunner {
    void log(Level level, String message, @Nullable Throwable throwable);

    @Nonnull
    IInterspaceManager getInterspaceManager();
}
