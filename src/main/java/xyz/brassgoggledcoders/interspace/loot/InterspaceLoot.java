package xyz.brassgoggledcoders.interspace.loot;

import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import xyz.brassgoggledcoders.interspace.Interspace;

public class InterspaceLoot {
    public static final LootParameterSet SPATIAL = LootParameterSets.register(Interspace.rl("spatial").toString(),
            builder -> builder.required(LootParameters.field_237457_g_)
                    .build());
}
