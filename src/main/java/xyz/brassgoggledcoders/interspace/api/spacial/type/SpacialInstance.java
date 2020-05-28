package xyz.brassgoggledcoders.interspace.api.spacial.type;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceQuery;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<List<SpacialItem>> remove(Map<String, String> queryMarkers) {
        return InterspaceAPI.getInterspaceClient().remove(new InterspaceQuery(this.getWorld(), this.getChunk(), queryMarkers));
    }
}
