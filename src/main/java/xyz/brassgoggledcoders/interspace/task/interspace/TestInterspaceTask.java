package xyz.brassgoggledcoders.interspace.task.interspace;

import org.apache.logging.log4j.Level;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.api.task.interspace.IInterspaceTaskRunner;
import xyz.brassgoggledcoders.interspace.api.task.interspace.InterspaceTask;
import xyz.brassgoggledcoders.interspace.content.InterspaceTaskTypes;

public class TestInterspaceTask extends InterspaceTask {
    public TestInterspaceTask(TaskType type) {
        super(type);
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    public void run(IInterspaceTaskRunner taskRunner) {
        taskRunner.log(Level.WARN, "Test Complete", null);
    }

    public static TestInterspaceTask create() {
        return new TestInterspaceTask(InterspaceTaskTypes.TEST_INTERSPACE.get());
    }
}
