package xyz.brassgoggledcoders.interspace.datagen.loot;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.interspace.loot.InterspaceLoot;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InterspaceLootTableProvider extends LootTableProvider {
    public InterspaceLootTableProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    @Nonnull
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return Lists.newArrayList(
                Pair.of(InterspaceSpatialLootTables::new, InterspaceLoot.SPATIAL)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationTracker validationtracker) {

    }

    @Override
    @Nonnull
    public String getName() {
        return "Interspace Loot Tables";
    }
}
