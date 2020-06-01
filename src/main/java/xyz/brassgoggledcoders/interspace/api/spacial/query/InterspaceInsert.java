package xyz.brassgoggledcoders.interspace.api.spacial.query;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItemType;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class InterspaceInsert {
    private final IWorld world;
    private final ChunkPos chunkPos;
    private final Collection<SpacialItem> spacialItems;

    public <T> InterspaceInsert(IWorld world, ChunkPos chunkPos, SpacialItemType<T> type, T item) {
        this(world, chunkPos, Collections.singleton(type.toSpacialItem(item)));
    }

    public <T> InterspaceInsert(IWorld world, ChunkPos chunkPos, SpacialItemType<T> type, Collection<T> items) {
        this(world, chunkPos, items.parallelStream()
                .map(type::toSpacialItem)
                .collect(Collectors.toList()));

    }

    public InterspaceInsert(IWorld world, ChunkPos chunkPos, Collection<SpacialItem> spacialItems) {
        this.world = world;
        this.chunkPos = chunkPos;
        this.spacialItems = spacialItems;
    }

    public Collection<SpacialItem> getSpacialItems() {
        return spacialItems;
    }

    public IWorld getWorld() {
        return world;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }
}
