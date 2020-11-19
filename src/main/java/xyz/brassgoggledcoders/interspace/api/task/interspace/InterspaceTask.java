package xyz.brassgoggledcoders.interspace.api.task.interspace;

import xyz.brassgoggledcoders.interspace.api.source.Source;
import xyz.brassgoggledcoders.interspace.api.task.Task;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;

public abstract class InterspaceTask extends Task<IInterspaceTaskRunner> {
    protected InterspaceTask(TaskType type, Source<?> source) {
        super(type, source);
    }
}
