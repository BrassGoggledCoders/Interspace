package xyz.brassgoggledcoders.interspace.api.task;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.interspace.api.source.Source;

import javax.annotation.Nonnull;

public abstract class Task<T extends ITaskRunner> implements INBTSerializable<CompoundNBT>, Comparable<Task<?>> {
    private final TaskType type;
    private final Source<?> source;

    protected Task(TaskType taskType, Source<?> source) {
        this.type = taskType;
        this.source = source;
    }

    /**
     * @return true if this task should be completed before any others are handled (Generally reserved for Database setup)
     */
    public boolean isBlocking() {
        return false;
    }

    public boolean shouldSave() {
        return true;
    }

    public abstract boolean isDone();

    public Source<?> getSource() {
        return source;
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
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("source", this.getSource().serializeNBT());
        return nbt;
    }

    public abstract void run(T taskRunner);

    @Override
    public int compareTo(@Nonnull Task<?> o) {
        return Integer.compare(this.getPriority(), o.getPriority());
    }

    public TaskType getType() {
        return type;
    }
}
