package xyz.brassgoggledcoders.interspace.datagen.loot;

import net.minecraft.item.Items;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import xyz.brassgoggledcoders.interspace.Interspace;

public class InterspaceSpatialLootTables extends SpatialLootTables {

    public InterspaceSpatialLootTables() {
        super(Interspace.ID);
    }

    @Override
    protected void addTables() {
        this.addTable("water_spring", LootTable.builder()
                .addLootPool(LootPool.builder()
                        .rolls(RandomValueRange.of(1, 10))
                        .addEntry(ItemLootEntry.builder(Items.WATER_BUCKET))
                )
        );
        this.addTable("lava_spring", LootTable.builder()
                .addLootPool(LootPool.builder()
                        .rolls(RandomValueRange.of(1, 10))
                        .addEntry(ItemLootEntry.builder(Items.LAVA_BUCKET))
                )
        );
    }
}
