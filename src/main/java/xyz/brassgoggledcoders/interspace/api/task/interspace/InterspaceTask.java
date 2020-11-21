package xyz.brassgoggledcoders.interspace.api.task.interspace;

import xyz.brassgoggledcoders.interspace.api.task.Task;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;

public abstract class InterspaceTask extends Task<IInterspaceTaskRunner> {
    public InterspaceTask(TaskType type) {
        super(type);
    }
}
