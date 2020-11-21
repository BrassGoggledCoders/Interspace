package xyz.brassgoggledcoders.interspace.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.task.interspace.SetupInterspaceTask;
import xyz.brassgoggledcoders.interspace.task.interspace.TestInterspaceTask;

public class InterspaceTaskTypes {

    public static final RegistryEntry<TaskType> SETUP_INTERSPACE = InterspaceMod.getRegistrate()
            .object("setup_interspace")
            .simple(TaskType.class, () -> TaskType.of(SetupInterspaceTask::new));

    public static final RegistryEntry<TaskType> TEST_INTERSPACE = InterspaceMod.getRegistrate()
            .object("test_interspace")
            .simple(TaskType.class, () -> TaskType.of(TestInterspaceTask::new));

    public static void setup() {

    }
}
