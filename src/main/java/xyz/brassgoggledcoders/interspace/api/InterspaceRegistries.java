package xyz.brassgoggledcoders.interspace.api;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.interspace.api.source.SourceType;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;

public class InterspaceRegistries {
    public static final IForgeRegistry<TaskType> TASK_TYPE = RegistryManager.ACTIVE.getRegistry(TaskType.class);

    public static final IForgeRegistry<SourceType> SOURCE_TYPE = RegistryManager.ACTIVE.getRegistry(SourceType.class);
}
