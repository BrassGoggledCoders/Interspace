package xyz.brassgoggledcoders.interspace.manager;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceManager;
import xyz.brassgoggledcoders.interspace.api.task.Task;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.api.task.interspace.InterspaceTask;
import xyz.brassgoggledcoders.interspace.api.task.world.WorldTask;
import xyz.brassgoggledcoders.interspace.sql.SQLClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

@EventBusSubscriber(modid = InterspaceMod.ID, bus = Bus.FORGE)
public class InterspaceManager implements IInterspaceManager {
    private static final FolderName FOLDER = new FolderName("interspace");

    private final Path path;
    private final InterspaceRunnable runnable;
    private final Thread thread;
    private final SQLClient sqlClient;

    private final Queue<InterspaceTask> interspaceTaskQueue;
    private final Queue<WorldTask> worldTaskQueue;

    public InterspaceManager(Path folderPath, SQLClient sqlClient, Queue<InterspaceTask> interspaceTaskQueue,
                             Queue<WorldTask> worldTaskQueue) {
        this.path = folderPath;
        this.sqlClient = sqlClient;

        this.interspaceTaskQueue = interspaceTaskQueue;
        this.worldTaskQueue = worldTaskQueue;
        this.runnable = new InterspaceRunnable(this, interspaceTaskQueue::poll,
                sqlClient, InterspaceMod.getServerConfig().maxRunningTasks.get());
        this.thread = new Thread(this.runnable);
    }

    public void start() {
        this.thread.setDaemon(true);
        this.thread.start();
    }

    @Override
    public boolean submitTask(InterspaceTask task) {
        return interspaceTaskQueue != null && interspaceTaskQueue.offer(task);
    }

    @Override
    public boolean submitTask(WorldTask task) {
        return worldTaskQueue != null && worldTaskQueue.offer(task);
    }

    public void close() {
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

        if (interspaceTaskQueue != null && !interspaceTaskQueue.isEmpty()) {
            if (path != null) {
                writeTaskQueueFile(interspaceTaskQueue, path.resolve("interspaceTaskQueue.nbt"));
            } else {
                InterspaceMod.LOGGER.fatal("Interspace Task Queue was not Empty, and Path was null");
            }
        }

        if (sqlClient != null) {
            try {
                sqlClient.close();
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
        while ((task = queue.poll()) != null) {
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

    public static InterspaceManager create(MinecraftServer minecraftServer) {
        Path path = minecraftServer.func_240776_a_(FOLDER);

        try {
            Files.createDirectories(path);
            SQLClient sqlClient = SQLClient.create(path);
            Queue<InterspaceTask> interspaceTaskQueue = new PriorityBlockingQueue<>();
            readTaskQueueFile(interspaceTaskQueue, path.resolve("interspaceTaskQueue.nbt"), InterspaceTask.class);
            Queue<WorldTask> worldTaskQueue = new PriorityBlockingQueue<>();
            readTaskQueueFile(worldTaskQueue, path.resolve("worldTaskQueue.nbt"), WorldTask.class);
            return new InterspaceManager(path, sqlClient, interspaceTaskQueue, worldTaskQueue);
        } catch (IOException | ClassNotFoundException | SQLException exception) {
            throw new IllegalStateException("Failed to Setup Interspace");
        }
    }
}
