package xyz.brassgoggledcoders.interspace.api.task;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

public abstract class Task<T extends ITaskRunner> implements INBTSerializable<CompoundNBT>, Comparable<Task<?>> {
    private final TaskType type;

    protected Task(TaskType taskType) {
        this.type = taskType;
    }

    public boolean shouldSave() {
        return true;
    }

    public boolean isDone() {
        return true;
    }

    /**
     * @return a priority. Higher numbers will be prioritized. Database setup is Priority 1000
     */
    public int getPriority() {
        return 0;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    public abstract void run(T taskRunner);

    @Override
    public int compareTo(@Nonnull Task<?> o) {
        return Integer.compare(o.getPriority(), this.getPriority());
    }

    public TaskType getType() {
        return type;
    }
}
