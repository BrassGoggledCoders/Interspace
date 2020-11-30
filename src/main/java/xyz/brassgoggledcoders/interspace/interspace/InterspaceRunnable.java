package xyz.brassgoggledcoders.interspace.interspace;

import com.google.common.collect.Lists;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceClient;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceManager;
import xyz.brassgoggledcoders.interspace.api.interspace.InterspaceVolume;
import xyz.brassgoggledcoders.interspace.api.mail.Mail;
import xyz.brassgoggledcoders.interspace.api.sql.ISQLClient;
import xyz.brassgoggledcoders.interspace.api.task.Task;
import xyz.brassgoggledcoders.interspace.api.task.interspace.IInterspaceTaskRunner;
import xyz.brassgoggledcoders.interspace.api.task.interspace.InterspaceTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class InterspaceRunnable implements Runnable, IInterspaceTaskRunner {
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final List<InterspaceTask> runningTasks = Lists.newArrayList();
    private final int maxRunningTasks;
    private final IInterspaceManager interspaceManager;
    private final Supplier<InterspaceTask> taskSupplier;
    private final ISQLClient sqlClient;
    private final IInterspaceClient interspaceClient;

    private InterspaceTask blockingTask;

    public InterspaceRunnable(IInterspaceManager interspaceManager, Supplier<InterspaceTask> taskSupplier,
                              ISQLClient sqlClient, int maxRunningTasks) {
        this.interspaceManager = interspaceManager;
        this.taskSupplier = taskSupplier;
        this.sqlClient = sqlClient;
        this.maxRunningTasks = maxRunningTasks;
        this.interspaceClient = new InterspaceClient(sqlClient);
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

        while (!runningTasks.isEmpty()) {
            runningTasks.removeIf(Task::isDone);
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

    @Nonnull
    @Override
    public IInterspaceClient getInterspaceClient() {
        return interspaceClient;
    }

    @Override
    @Nonnull
    public IInterspaceManager getInterspaceManager() {
        return this.interspaceManager;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean sendMail(ResourceLocation world, UUID address, Mail mail) {
        return InterspaceAPI.getPostOffice().sendMail(RegistryKey.getOrCreateKey(Registry.WORLD_KEY, world),
                address, mail);
    }

    @Override
    public void log(Level level, String message, @Nullable Throwable throwable) {
        InterspaceMod.LOGGER.log(level, message, throwable);
    }
}
