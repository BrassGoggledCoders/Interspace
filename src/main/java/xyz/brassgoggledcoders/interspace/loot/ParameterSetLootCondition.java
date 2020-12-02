package xyz.brassgoggledcoders.interspace.loot;

import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.conditions.ILootCondition;

import javax.annotation.Nonnull;

public class ParameterSetLootCondition implements ILootCondition {
    private final LootParameterSet parameterSet;

    public ParameterSetLootCondition(LootParameterSet parameterSet) {
        this.parameterSet = parameterSet;
    }

    @Nonnull
    @Override
    public LootConditionType func_230419_b_() {
        return null;
    }

    @Override
    public boolean test(LootContext lootContext) {
        return lootContext.lootTables.stream()
                .findFirst()
                .filter(lootTable -> lootTable.getParameterSet() == parameterSet)
                .isPresent();
    }
}
