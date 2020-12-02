package xyz.brassgoggledcoders.interspace.api;

import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.ResourceLocation;

public class CacheLootTables {
    public static final LootParameterSet PARAMETER_SET = LootParameterSets.register("interspace:cache",
            builder -> builder.required(LootParameters.field_237457_g_)
                    .optional(LootParameters.THIS_ENTITY)
                    .build()
    );

    public static ResourceLocation JUNK = rl("junk");
    public static ResourceLocation SUPPLIES = rl("supplies");
    public static ResourceLocation SHIPPING = rl("shipping");
    public static ResourceLocation DEAD_ADVENTURER = rl("dead_adventurer");
    public static ResourceLocation LOST_TREASURE = rl("lost_treasure");

    private static ResourceLocation rl(String path) {
        return new ResourceLocation("interspace", "cache/" + path);
    }
}
