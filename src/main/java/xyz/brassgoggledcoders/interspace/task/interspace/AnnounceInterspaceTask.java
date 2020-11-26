package xyz.brassgoggledcoders.interspace.task.interspace;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.api.task.interspace.IInterspaceTaskRunner;
import xyz.brassgoggledcoders.interspace.api.task.interspace.InterspaceTask;
import xyz.brassgoggledcoders.interspace.content.InterspaceTaskTypes;
import xyz.brassgoggledcoders.interspace.mail.SendMessageMail;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AnnounceInterspaceTask extends InterspaceTask {
    private CompletableFuture<Void> queryResult;
    private ChunkPos queryPos;
    private ResourceLocation world;
    private UUID address;

    public AnnounceInterspaceTask(TaskType type) {
        super(type);
    }

    public AnnounceInterspaceTask(ResourceLocation world, ChunkPos queryPos, UUID address) {
        super(InterspaceTaskTypes.ANNOUNCE_INTERSPACE_TASK.get());
        this.world = world;
        this.queryPos = queryPos;
        this.address = address;
    }

    @Override
    public boolean isDone() {
        return queryResult != null && queryResult.isDone();
    }

    @Override
    public void run(IInterspaceTaskRunner taskRunner) {
        if (world != null && address != null && queryPos != null) {
            queryResult = taskRunner.getInterspaceClient()
                    .query(world, queryPos)
                    .thenAccept(interspace -> taskRunner.sendMail(world, address, new SendMessageMail(
                            interspace.getAdjective()
                    )));
        } else {
            queryResult = CompletableFuture.completedFuture(null);
        }
    }

    @Override
    public boolean shouldSave() {
        return false;
    }
}
