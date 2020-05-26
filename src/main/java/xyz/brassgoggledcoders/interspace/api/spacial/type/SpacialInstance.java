package xyz.brassgoggledcoders.interspace.api.spacial.type;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.util.INBTSerializable;

public class SpacialInstance implements INBTSerializable<CompoundNBT> {
    private final SpacialType spacialType;

    private final IWorld world;
    private final IChunk chunk;

    public SpacialInstance(SpacialType spacialType, IWorld world, IChunk chunk) {
        this.spacialType = spacialType;
        this.world = world;
        this.chunk = chunk;
    }

    public void onLoad() {

    }

    public void tick() {

    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    protected IWorld getWorld() {
        return world;
    }

    protected IChunk getChunk() {
        return chunk;
    }

    public SpacialType getType() {
        return this.spacialType;
    }
}
