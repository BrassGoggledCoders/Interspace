package xyz.brassgoggledcoders.interspace.registrate;

import com.google.common.collect.Maps;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider.LootType;
import com.tterrag.registrate.providers.loot.RegistrateLootTables;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.interspace.api.CacheLootTables;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RegistrateCacheLootTables implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>, RegistrateLootTables {
    public static final LootType<RegistrateCacheLootTables> CACHE_LOOT_TYPE = LootType.register("cache",
            CacheLootTables.PARAMETER_SET, RegistrateCacheLootTables::new);
    private final Map<ResourceLocation, LootTable.Builder> builders;
    private final Consumer<RegistrateCacheLootTables> callback;

    public RegistrateCacheLootTables(AbstractRegistrate<?> parent, Consumer<RegistrateCacheLootTables> callback) {
        this.callback = callback;
        this.builders = Maps.newHashMap();
    }

    public void addTables() {
        this.callback.accept(this);
    }

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> resourceLocationBuilderBiConsumer) {
        this.addTables();
        this.builders.forEach(resourceLocationBuilderBiConsumer);
    }

    public void registerLootTable(ResourceLocation resourceLocation, Consumer<LootTable.Builder> setup) {
        LootTable.Builder builder = new LootTable.Builder()
                .setParameterSet(CacheLootTables.PARAMETER_SET);
        setup.accept(builder);
        this.builders.put(resourceLocation, builder);
    }
}
