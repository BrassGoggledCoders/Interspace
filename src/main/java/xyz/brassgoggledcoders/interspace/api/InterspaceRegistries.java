package xyz.brassgoggledcoders.interspace.api;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;

public class InterspaceRegistries {
    public static final IForgeRegistry<TaskType> TASK_TYPE = RegistryManager.ACTIVE.getRegistry(TaskType.class);
}
