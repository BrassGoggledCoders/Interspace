package xyz.brassgoggledcoders.interspace.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.task.interspace.SetupInterspaceTask;

public class InterspaceTaskTypes {

    public static final RegistryEntry<TaskType> SETUP_INTERSPACE = InterspaceMod.getRegistrate()
            .object("setup_interspace")
            .simple(TaskType.class, () -> TaskType.of(SetupInterspaceTask::new));

    public static void setup() {

    }
}
