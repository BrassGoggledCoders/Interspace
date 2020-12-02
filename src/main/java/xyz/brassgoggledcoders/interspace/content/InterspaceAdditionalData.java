package xyz.brassgoggledcoders.interspace.content;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.interspace.api.CacheLootTables;
import xyz.brassgoggledcoders.interspace.registrate.RegistrateCacheLootTables;

public class InterspaceAdditionalData {
    private static final String[] VOLUMES = new String[]{
            "Zero", "Sandbar", "Sandbar", "Sandbar", "Sandbar", "Transient",
            "Shallow", "Average", "Shelf", "Deep", "Abyssal", "Cosmic",
    };

    public static void generateText(RegistrateLangProvider provider) {
        int i = 0;
        for (String volume : VOLUMES) {
            provider.add("text.interspace.volume." + i++, volume);
        }
    }

    public static void generateCacheLootTables(RegistrateCacheLootTables cacheLootTables) {
        cacheLootTables.registerLootTable(CacheLootTables.JUNK, builder ->
                builder.addLootPool(new LootPool.Builder()
                        .rolls(RandomValueRange.of(2, 5))
                        .addEntry(TagLootEntry.getBuilder(ItemTags.LOGS)
                                .weight(30)
                                .acceptFunction(SetCount.builder(RandomValueRange.of(16, 64)))
                        )
                        .addEntry(ItemLootEntry.builder(Items.STONE)
                                .weight(20)
                                .acceptFunction(SetCount.builder(RandomValueRange.of(20, 24)))
                        )
                        .addEntry(TagLootEntry.getBuilder(Tags.Items.GLASS)
                                .weight(10)
                                .acceptFunction(SetCount.builder(RandomValueRange.of(8, 16)))
                        )
                ).addLootPool(new LootPool.Builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(InterspaceItems.NAFASI_NUGGET.get())
                                .acceptFunction(SetCount.builder(RandomValueRange.of(0, 2)))
                        )
                )
        );
    }
}
