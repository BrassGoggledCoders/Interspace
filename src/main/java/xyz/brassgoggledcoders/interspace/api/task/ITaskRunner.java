package xyz.brassgoggledcoders.interspace.api.task;

import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceManager;
import xyz.brassgoggledcoders.interspace.api.mail.Mail;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public interface ITaskRunner {
    void log(Level level, String message, @Nullable Throwable throwable);

    @Nonnull
    IInterspaceManager getInterspaceManager();

    @ParametersAreNonnullByDefault
    boolean sendMail(ResourceLocation world, UUID address, Mail mail);
}
