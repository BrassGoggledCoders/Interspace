package xyz.brassgoggledcoders.interspace.manager;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceManager;
import xyz.brassgoggledcoders.interspace.api.task.Task;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.api.task.interspace.InterspaceTask;
import xyz.brassgoggledcoders.interspace.api.task.world.WorldTask;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

@EventBusSubscriber(modid = InterspaceMod.ID, bus = Bus.FORGE)
public class InterspaceManager implements IInterspaceManager {
    public static final InterspaceManager INSTANCE = new InterspaceManager();

    private static final FolderName FOLDER = new FolderName("interspace");
    private static Path path;
    private static Connection connection;
    private static InterspaceRunnable runnable;
    private static Thread thread;

    private static Queue<InterspaceTask> interspaceTaskQueue;
    private static Queue<WorldTask> worldTaskQueue;

    @Nonnull
    public static Connection getConnection() {
        return Objects.requireNonNull(connection, "Interspace Connection not Setup");
    }

    @Nonnull
    public static InterspaceRunnable getRunnable() {
        return Objects.requireNonNull(runnable, "Interspace Runnable not Setup");
    }

    @Nonnull
    public static Queue<InterspaceTask> getInterspaceTaskQueue() {
        return Objects.requireNonNull(interspaceTaskQueue, "Interspace Task Queue not Setup");
    }

    @Nonnull
    public static Queue<WorldTask> getWorldTaskQueue() {
        return Objects.requireNonNull(worldTaskQueue, "Interspace World Task Queue not Setup");
    }

    public static boolean isReady() {
        return runnable != null;
    }

    public static void serverStarted(FMLServerStartedEvent event) {
        if (runnable != null) {
            thread = new Thread(runnable, InterspaceMod.ID);
            thread.setDaemon(true);
            thread.start();
        }
    }

    @SubscribeEvent
    public static void serverStarting(FMLServerAboutToStartEvent event) {
        close();

        path = event.getServer().func_240776_a_(FOLDER);

        try {
            Files.createDirectories(path);
            Class.forName("org.sqlite.JDBC");

            SQLiteConnectionPoolDataSource dataSource = new SQLiteConnectionPoolDataSource();
            dataSource.setUrl("jdbc:sqlite:" + path.resolve("main.db").toString());
            connection = dataSource.getConnection();
            runnable = new InterspaceRunnable();
            interspaceTaskQueue = new PriorityBlockingQueue<>();
            readTaskQueueFile(interspaceTaskQueue, path.resolve("interspaceTaskQueue.nbt"), InterspaceTask.class);
            worldTaskQueue = new PriorityBlockingQueue<>();
            readTaskQueueFile(worldTaskQueue, path.resolve("worldTaskQueue.nbt"), WorldTask.class);

        } catch (IOException | ClassNotFoundException | SQLException exception) {
            InterspaceMod.LOGGER.fatal("Failed to Setup Interspace", exception);
        }
    }

    @SubscribeEvent
    public static void serverStopping(FMLServerStoppingEvent event) {
        close();
    }

    private static void close() {
        if (runnable != null) {
            runnable.close();
        }

        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException exception) {
                InterspaceMod.LOGGER.fatal("Failed to Close Interspace Thread", exception);
            }
        }

        if (worldTaskQueue != null && !worldTaskQueue.isEmpty()) {
            if (path != null) {
                writeTaskQueueFile(worldTaskQueue, path.resolve("worldTaskQueue.nbt"));
            } else {
                InterspaceMod.LOGGER.fatal("Interspace World Task Queue was not Empty, and Path was null");
            }
        }
        worldTaskQueue = null;

        if (interspaceTaskQueue != null && !interspaceTaskQueue.isEmpty()) {
            if (path != null) {
                writeTaskQueueFile(interspaceTaskQueue, path.resolve("interspaceTaskQueue.nbt"));
            } else {
                InterspaceMod.LOGGER.fatal("Interspace Task Queue was not Empty, and Path was null");
            }
        }
        interspaceTaskQueue = null;

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException exception) {
                InterspaceMod.LOGGER.warn("Failed to Close Interspace Connection", exception);
            }
        }
    }

    @SuppressWarnings("ignored")
    private static <T extends Task<?>> void readTaskQueueFile(Queue<T> queue, Path path, Class<T> tClass) {
        File file = path.toFile();
        if (file.exists()) {
            try {
                CompoundNBT nbt = CompressedStreamTools.readCompressed(file);
                if (nbt.contains("queue")) {
                    ListNBT queueNBT = nbt.getList("queue", Constants.NBT.TAG_COMPOUND);
                    for (int i = 0; i < queueNBT.size(); i++) {
                        Task<?> task = TaskType.deserializeTask(queueNBT.getCompound(i));
                        if (tClass.isInstance(task)) {
                            queue.offer(tClass.cast(task));
                        }
                    }
                }
                file.delete();
            } catch (IOException exception) {
                InterspaceMod.LOGGER.fatal("Failed to read File for Task Queue", exception);
            }
        }
    }

    private static <T extends Task<?>> void writeTaskQueueFile(Queue<T> queue, Path path) {
        ListNBT queueNBT = new ListNBT();
        T task;
        while((task = queue.poll()) != null) {
            if (task.shouldSave()) {
                queueNBT.add(TaskType.serializeTask(task));
            }
        }


        if (!queueNBT.isEmpty()) {
            try {
                Files.createFile(path);
                CompoundNBT nbt = new CompoundNBT();
                nbt.put("queue", queueNBT);
                CompressedStreamTools.writeCompressed(nbt, path.toFile());
            } catch (IOException exception) {
                InterspaceMod.LOGGER.fatal("Failed to Write Task Queue", exception);
            }
        }
    }

    @Override
    public boolean submitTask(InterspaceTask task) {
        return interspaceTaskQueue != null && interspaceTaskQueue.offer(task);
    }

    @Override
    public boolean submitTask(WorldTask task) {
        return worldTaskQueue != null && worldTaskQueue.offer(task);
    }
}
