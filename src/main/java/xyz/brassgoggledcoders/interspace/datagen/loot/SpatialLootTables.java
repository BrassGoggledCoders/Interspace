package xyz.brassgoggledcoders.interspace.datagen.loot;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import xyz.brassgoggledcoders.interspace.Interspace;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class SpatialLootTables implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
    private final Map<ResourceLocation, LootTable.Builder> tables;
    private final String modid;

    public SpatialLootTables(String modid) {
        this.modid = modid;
        this.tables = Maps.newHashMap();
    }

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        this.addTables();
        tables.forEach(consumer);
    }

    private ResourceLocation rl(String name) {
        return new ResourceLocation(modid, "spatial/" + name);
    }

    protected void addTable(String name, LootTable.Builder builder) {
        tables.put(this.rl(name), builder);
    }

    protected abstract void addTables();
}
