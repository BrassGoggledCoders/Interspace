package xyz.brassgoggledcoders.interspace.manager;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.Level;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceManager;
import xyz.brassgoggledcoders.interspace.api.sql.ISQLClient;
import xyz.brassgoggledcoders.interspace.api.task.Task;
import xyz.brassgoggledcoders.interspace.api.task.interspace.IInterspaceTaskRunner;
import xyz.brassgoggledcoders.interspace.api.task.interspace.InterspaceTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class InterspaceRunnable implements Runnable, IInterspaceTaskRunner {
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final List<InterspaceTask> runningTasks = Lists.newArrayList();
    private final int maxRunningTasks;
    private final IInterspaceManager interspaceManager;
    private final Supplier<InterspaceTask> taskSupplier;
    private final ISQLClient sqlClient;

    private InterspaceTask blockingTask;

    public InterspaceRunnable(IInterspaceManager interspaceManager, Supplier<InterspaceTask> taskSupplier,
                              ISQLClient sqlClient, int maxRunningTasks) {
        this.interspaceManager = interspaceManager;
        this.taskSupplier = taskSupplier;
        this.sqlClient = sqlClient;
        this.maxRunningTasks = maxRunningTasks;
    }

    @Override
    public void run() {
        while (running.get()) {
            runningTasks.removeIf(Task::isDone);
            if (blockingTask == null) {
                int availableTaskSpace = maxRunningTasks - runningTasks.size();
                InterspaceTask currentTask;
                while (blockingTask == null && availableTaskSpace > 0 && (currentTask = taskSupplier.get()) != null) {
                    availableTaskSpace--;
                    currentTask.run(this);
                    runningTasks.add(currentTask);
                    if (currentTask.isBlocking()) {
                        blockingTask = currentTask;
                    }
                }
            } else if (blockingTask.isDone()) {
                blockingTask = null;
            }
        }
    }

    public void close() {
        running.set(false);
    }

    @Override
    @Nonnull
    public ISQLClient getSQLClient() {
        return sqlClient;
    }

    @Override
    @Nonnull
    public IInterspaceManager getManager() {
        return this.interspaceManager;
    }

    @Override
    public void log(Level level, String message, @Nullable Throwable throwable) {
        InterspaceMod.LOGGER.log(level, message, throwable);
    }
}
