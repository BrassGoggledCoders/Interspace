package xyz.brassgoggledcoders.interspace.api.task;

import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;

public interface ITaskRunner {
    void log(Level level, String message, @Nullable Throwable throwable);
}
