package xyz.brassgoggledcoders.interspace.api.spacial.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.Transaction;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface IInterspaceWorld extends IInterspace, INBTSerializable<CompoundNBT> {
    void tick();

    void onChunkLoad(@Nonnull IChunk chunk);

    void onChunkUnload(@Nonnull IChunk chunk);

    Transaction<Collection<SpacialItem>> offer(ChunkPos chunkPos, Collection<SpacialItem> offered);

    @Nonnull
    SpacialInstance getSpacialInstance(ChunkPos chunk);
}
