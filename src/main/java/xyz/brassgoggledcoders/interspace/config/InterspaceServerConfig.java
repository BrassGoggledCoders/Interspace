package xyz.brassgoggledcoders.interspace.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class InterspaceServerConfig {
    public final ForgeConfigSpec.IntValue maxRunningTasks;

    public InterspaceServerConfig(ForgeConfigSpec.Builder configSpec) {
        maxRunningTasks = configSpec.defineInRange("maxRunningTasks", 1000, 1, Integer.MAX_VALUE);
    }


}
