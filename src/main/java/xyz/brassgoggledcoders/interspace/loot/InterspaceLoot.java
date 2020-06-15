package xyz.brassgoggledcoders.interspace.loot;

import net.minecraft.world.storage.loot.LootParameterSet;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import xyz.brassgoggledcoders.interspace.Interspace;

public class InterspaceLoot {
    public static final LootParameterSet SPATIAL = LootParameterSets.register(Interspace.rl("spacial").toString(),
            builder -> builder.required(LootParameters.POSITION)
                    .build());
}
