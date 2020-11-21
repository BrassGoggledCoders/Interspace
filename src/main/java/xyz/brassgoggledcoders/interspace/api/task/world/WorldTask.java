package xyz.brassgoggledcoders.interspace.api.task.world;

import xyz.brassgoggledcoders.interspace.api.task.Task;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;

public abstract class WorldTask extends Task<IWorldTaskRunner> {
    public WorldTask(TaskType type) {
        super(type);
    }
}
