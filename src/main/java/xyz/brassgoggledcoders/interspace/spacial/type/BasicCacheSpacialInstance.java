package xyz.brassgoggledcoders.interspace.spacial.type;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialType;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpacialItemTypes;
import xyz.brassgoggledcoders.interspace.loot.InterspaceLoot;

public class BasicCacheSpacialInstance extends SpacialInstance {
    private ResourceLocation lootTable = null;

    public BasicCacheSpacialInstance(SpacialType spacialType, IWorld world, ChunkPos chunkPos) {
        super(spacialType, world, chunkPos);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (lootTable != null) {
            if (this.getWorld() instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) this.getWorld();
                LootContext lootContext = new LootContext.Builder(serverWorld)
                        .withParameter(LootParameters.POSITION, this.getChunkPos().asBlockPos())
                        .build(InterspaceLoot.BASIC_CACHE);
                this.offer(InterspaceSpacialItemTypes.ITEM_STACK.get()
                        .convertCollection(serverWorld.getServer()
                                .getLootTableManager()
                                .getLootTableFromLocation(lootTable)
                                .generate(lootContext))
                );
            }
            lootTable = null;
        }
    }

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
