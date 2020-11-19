package xyz.brassgoggledcoders.interspace.api.task.world;

import xyz.brassgoggledcoders.interspace.api.source.Source;
import xyz.brassgoggledcoders.interspace.api.task.Task;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;

public abstract class WorldTask extends Task<IWorldTaskRunner> {
    protected WorldTask(TaskType type, Source<?> source) {
        super(type, source);
    }
}
