package xyz.brassgoggledcoders.interspace;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.task.interspace.TestInterspaceTask;

public class InterspaceCommand {
    public static LiteralArgumentBuilder<CommandSource> create() {
        return LiteralArgumentBuilder.<CommandSource>literal("interspace")
                .then(
                        LiteralArgumentBuilder.<CommandSource>literal("test")
                                .requires(commandSource -> commandSource.getEntity() != null)
                                .executes(context -> {
                                    InterspaceAPI.getManager().submitTask(TestInterspaceTask.create());
                                    return 1;
                                })
                );
    }
}
