package xyz.brassgoggledcoders.interspace.api.task;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.interspace.api.InterspaceRegistries;

import java.util.Objects;
import java.util.function.Function;

public class TaskType extends ForgeRegistryEntry<TaskType> {
    private final Function<TaskType, Task<?>> taskCreator;

    public TaskType(Function<TaskType, Task<?>> taskCreator) {
        this.taskCreator = taskCreator;
    }

    public Task<?> createTask() {
        return this.taskCreator.apply(this);
    }

    public static TaskType of(Function<TaskType, Task<?>> taskCreator) {
        return new TaskType(taskCreator);
    }

    public static Task<?> deserializeTask(CompoundNBT nbt) {
        TaskType type = InterspaceRegistries.TASK_TYPE.getValue(new ResourceLocation(nbt.getString("type")));
        if (type != null) {
            Task<?> task = type.createTask();
            task.deserializeNBT(nbt);
            return task;
        }
        return null;
    }

    public static CompoundNBT serializeTask(Task<?> task) {
        CompoundNBT nbt = task.serializeNBT();
        nbt.putString("type", Objects.requireNonNull(task.getType().getRegistryName()).toString());
        return nbt;
    }
}
