package xyz.brassgoggledcoders.interspace.spatial.type;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpatialItemTypes;

import java.util.Collection;
import java.util.List;

public class BasicCacheSpatialInstance extends LootTableSpatialInstance {
    public BasicCacheSpatialInstance(SpatialType spatialType, IWorld world, ChunkPos chunkPos) {
        super(spatialType, world, chunkPos);
    }

    @Override
    protected Collection<SpatialItem> convertEntries(List<ItemStack> lootList) {
        return InterspaceSpatialItemTypes.ITEM_STACK.get()
                .convertCollection(lootList);
    }
}
