package xyz.brassgoggledcoders.interspace.api.task;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.interspace.api.InterspaceRegistries;
import xyz.brassgoggledcoders.interspace.api.source.Source;
import xyz.brassgoggledcoders.interspace.api.source.SourceType;

import java.util.Objects;
import java.util.function.BiFunction;

public class TaskType extends ForgeRegistryEntry<TaskType> {
    private final BiFunction<TaskType, Source<?>, Task<?>> taskCreator;

    public TaskType(BiFunction<TaskType, Source<?>, Task<?>> taskCreator) {
        this.taskCreator = taskCreator;
    }

    public Task<?> createTask(Source<?> source) {
        return this.taskCreator.apply(this, source);
    }

    public static TaskType of(BiFunction<TaskType, Source<?>, Task<?>> taskCreator) {
        return new TaskType(taskCreator);
    }

    public static Task<?> deserializeTask(CompoundNBT nbt) {
        TaskType type = InterspaceRegistries.TASK_TYPE.getValue(new ResourceLocation(nbt.getString("type")));
        if (type != null) {
            Source<?> source = SourceType.deserializeSource(nbt.getCompound("source"));
            if (source != null) {
                Task<?> task = type.createTask(source);
                task.deserializeNBT(nbt);
                return task;
            }
        }
        return null;
    }

    public static CompoundNBT serializeTask(Task<?> task) {
        CompoundNBT nbt = task.serializeNBT();
        nbt.putString("type", Objects.requireNonNull(task.getType().getRegistryName()).toString());
        nbt.put("source", SourceType.serializeSource(task.getSource()));
        return nbt;
    }
}
