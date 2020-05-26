package xyz.brassgoggledcoders.interspace.loot;

import net.minecraft.world.storage.loot.LootParameterSet;
import net.minecraft.world.storage.loot.LootParameters;

public class InterspaceLoot {
    public static final LootParameterSet BASIC_CACHE = new LootParameterSet.Builder()
            .required(LootParameters.POSITION)
            .build();
}
