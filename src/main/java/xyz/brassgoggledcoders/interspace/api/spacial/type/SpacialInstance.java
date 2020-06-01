package xyz.brassgoggledcoders.interspace.api.spacial.type;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
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
    private final ChunkPos chunkPos;

    public SpacialInstance(SpacialType spacialType, IWorld world, ChunkPos chunkPos) {
        this.spacialType = spacialType;
        this.world = world;
        this.chunkPos = chunkPos;
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

    public IWorld getWorld() {
        return world;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public SpacialType getType() {
        return this.spacialType;
    }

    public CompletableFuture<List<SpacialItem>> remove(Map<String, String> queryMarkers) {
        return InterspaceAPI.getInterspaceClient().remove(new InterspaceQuery(this.getWorld(), this.getChunkPos(), queryMarkers));
    }
}
