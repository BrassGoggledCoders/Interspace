package xyz.brassgoggledcoders.interspace.api.spatial.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialInstance;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface ISpatialWorld extends ISpatial, INBTSerializable<CompoundNBT> {
    void tick();

    void onChunkLoad(@Nonnull IChunk chunk);

    void onChunkUnload(@Nonnull IChunk chunk);

    Transaction<Collection<SpatialItem>> offer(ChunkPos chunkPos, Collection<SpatialItem> offered);

    @Nonnull
    SpatialInstance getSpacialInstance(ChunkPos chunk);
}
