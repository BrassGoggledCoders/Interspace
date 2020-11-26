package xyz.brassgoggledcoders.interspace.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.task.interspace.AnnounceInterspaceTask;
import xyz.brassgoggledcoders.interspace.task.interspace.SetupChunkInterspaceTask;
import xyz.brassgoggledcoders.interspace.task.interspace.SetupWorldInterspaceTask;
import xyz.brassgoggledcoders.interspace.task.interspace.TestInterspaceTask;

public class InterspaceTaskTypes {

    public static final RegistryEntry<TaskType> SETUP_WORLD_INTERSPACE = InterspaceMod.getRegistrate()
            .object("setup_world_interspace")
            .simple(TaskType.class, () -> TaskType.of(SetupWorldInterspaceTask::new));

    public static final RegistryEntry<TaskType> SETUP_CHUNK_INTERSPACE = InterspaceMod.getRegistrate()
            .object("setup_chunk_interspace")
            .simple(TaskType.class, () -> TaskType.of(SetupChunkInterspaceTask::new));

    public static final RegistryEntry<TaskType> TEST_INTERSPACE = InterspaceMod.getRegistrate()
            .object("test_interspace")
            .simple(TaskType.class, () -> TaskType.of(TestInterspaceTask::new));

    public static final RegistryEntry<TaskType> ANNOUNCE_INTERSPACE_TASK = InterspaceMod.getRegistrate()
            .object("announce_interspace")
            .simple(TaskType.class, () -> TaskType.of(AnnounceInterspaceTask::new));

    public static void setup() {

    }
}
