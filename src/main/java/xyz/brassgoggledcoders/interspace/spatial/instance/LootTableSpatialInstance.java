package xyz.brassgoggledcoders.interspace.spatial.instance;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialInstance;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;
import xyz.brassgoggledcoders.interspace.loot.InterspaceLoot;

import java.util.Collection;
import java.util.List;

public abstract class LootTableSpatialInstance extends SpatialInstance {
    private ResourceLocation lootTable = null;

    protected LootTableSpatialInstance(SpatialType spatialType, IWorld world, ChunkPos chunkPos) {
        super(spatialType, world, chunkPos);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (lootTable != null) {
            if (this.getWorld() instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) this.getWorld();
                LootContext lootContext = new LootContext.Builder(serverWorld)
                        .withParameter(LootParameters.field_237457_g_, Vector3d.copyCentered(this.getChunkPos().asBlockPos()))
                        .build(InterspaceLoot.SPATIAL);
                this.offer(this.convertEntries(serverWorld.getServer()
                        .getLootTableManager()
                        .getLootTableFromLocation(lootTable)
                        .generate(lootContext)
                ));
            }
            lootTable = null;
        }
    }

    protected abstract Collection<SpatialItem> convertEntries(List<ItemStack> lootList);

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.lootTable = nbt.contains("lootTable") ? new ResourceLocation(nbt.getString("lootTable")) : null;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = super.serializeNBT();
        if (lootTable != null) {
            compoundNBT.putString("lootTable", lootTable.toString());
        }
        return compoundNBT;
    }
}
