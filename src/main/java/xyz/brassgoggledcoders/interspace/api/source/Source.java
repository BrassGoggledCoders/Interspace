package xyz.brassgoggledcoders.interspace.api.source;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public abstract class Source<T> implements INBTSerializable<CompoundNBT> {
    private final SourceType type;

    protected Source(SourceType type) {
        this.type = type;
    }

    @Nullable
    public abstract T locate(ServerWorld serverWorld);

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    public SourceType getType() {
        return type;
    }
}
