package xyz.brassgoggledcoders.interspace.api.interspace;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.interspace.api.mail.Mail;
import xyz.brassgoggledcoders.interspace.api.mail.MailBoxStatus;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.UUID;

public interface IInterspacePostOffice {
    /**
     * A version of sendMail that is not tied to worlds, useful for things like Players/Entities where location is not constant,
     * all general mailboxes are held on the overworld
     *
     * @param address the address of the box to send to
     * @param mail    the mail message
     * @return if the send was successful, generally false means the address didn't exist
     */
    boolean sendMail(@Nonnull UUID address, @Nonnull Mail mail);

    /**
     * A version of sendMail where storage is tied to worlds, generally should be used for Blocks
     *
     * @param world   the world in which the mailbox exists
     * @param address the address of the box to send to
     * @param mail    the mail message
     * @return if the send was successful, generally false means the address didn't exist
     */
    boolean sendMail(@Nonnull RegistryKey<World> world, @Nonnull UUID address, @Nonnull Mail mail);

    /**
     * Receive all Mail from a Box that is this world
     *
     * @param world       the World where this box exists (Use World.OVERWORLD for nonspecific)
     * @param address     the Address of the box to pull from
     * @param maxReceived the max pieces of mail to receive
     * @return any Mail for the address
     */
    @Nonnull
    Collection<Mail> receiveMail(@Nonnull RegistryKey<World> world, @Nonnull UUID address, int maxReceived);

    /**
     * Creates a Mail box
     *
     * @param world   the world to create the mailbox in
     * @param address the address of the mailbox
     * @return mailbox statue
     */
    MailBoxStatus createMailBox(@Nonnull RegistryKey<World> world, @Nonnull UUID address);
}
