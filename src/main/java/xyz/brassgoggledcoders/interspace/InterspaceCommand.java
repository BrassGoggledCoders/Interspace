package xyz.brassgoggledcoders.interspace;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspacePostOffice;
import xyz.brassgoggledcoders.interspace.mail.SendMessageMail;
import xyz.brassgoggledcoders.interspace.task.interspace.TestInterspaceTask;

import java.util.Collection;

public class InterspaceCommand {
    public static LiteralArgumentBuilder<CommandSource> create() {
        return LiteralArgumentBuilder.<CommandSource>literal("interspace")
                .then(LiteralArgumentBuilder.<CommandSource>literal("test")
                        .executes(context -> {
                            InterspaceAPI.getManager().submitTask(TestInterspaceTask.create());
                            return 1;
                        })
                )
                .then(mail());
    }

    private static LiteralArgumentBuilder<CommandSource> mail() {
        return LiteralArgumentBuilder.<CommandSource>literal("mail")
                .then(LiteralArgumentBuilder.<CommandSource>literal("entity")
                        .then(Commands.argument("entities", EntityArgument.entities())
                                .then(Commands.argument("message", MessageArgument.message())
                                        .executes(context -> {
                                            Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
                                            ITextComponent message = MessageArgument.getMessage(context, "message");
                                            int mailSent = 0;
                                            IInterspacePostOffice postOffice = InterspaceAPI.getPostOffice();
                                            for (Entity entity : entities) {
                                                if (postOffice.sendMail(entity.getUniqueID(), new SendMessageMail(message))) {
                                                    mailSent++;
                                                }
                                            }
                                            return mailSent;
                                        }))
                        )
                );
    }
}
