package xyz.brassgoggledcoders.interspace.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class InterspaceServerConfig {
    private final ForgeConfigSpec.IntValue maxRunningTasks;
    private final ForgeConfigSpec.DoubleValue defaultCacheChance;

    public InterspaceServerConfig(ForgeConfigSpec.Builder configSpec) {
        maxRunningTasks = configSpec.worldRestart()
                .defineInRange("maxRunningTasks", 1000, 1, Integer.MAX_VALUE);
        defaultCacheChance = configSpec.defineInRange("defaultCacheChance", 0.125D, 0, 1);
    }

    public Double getDefaultCacheChance() {
        return defaultCacheChance.get();
    }

    public Integer getMaxRunningTasks() {
        return maxRunningTasks.get();
    }
}
