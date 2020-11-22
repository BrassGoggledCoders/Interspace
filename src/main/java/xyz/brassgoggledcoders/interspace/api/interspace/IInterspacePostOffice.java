package xyz.brassgoggledcoders.interspace.api.interspace;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.interspace.api.mail.Mail;

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
     * @param address the address of the box to send to
     * @param mail    the mail message
     * @return if the send was successful, generally false means the address didn't exist
     */
    boolean sendMail(@Nonnull RegistryKey<World> world, @Nonnull UUID address, @Nonnull Mail mail);

    /**
     * Receive all Mail from a Box that is not tied to any world (Stored on Overworld)
     *
     * @param address the Address of the box to pull from
     * @return any Mail for the address
     */
    @Nonnull
    Collection<Mail> receiveMail(@Nonnull UUID address);

    /**
     * Receive all Mail from a Box that is this world
     *
     * @param world   the World where this box exists
     * @param address the Address of the box to pull from
     * @return any Mail for the address
     */
    @Nonnull
    Collection<Mail> receiveMail(@Nonnull RegistryKey<World> world, @Nonnull UUID address);

    /**
     * Creates a Mailbox not tied to world
     *
     * @return address of the Mailbox
     */
    UUID createMailBox();

    /**
     * Creates a Mail box tied to a specific world
     *
     * @param world The world to create for
     * @return address of the Mailbox
     */
    UUID createMailBox(@Nonnull RegistryKey<World> world);

    /**
     * Creates a Mail box tied to an existing address
     *
     * @param address an existing Address,
     * @return whether the mailbox was created
     */
    boolean createMailBox(@Nonnull UUID address);

    /**
     * Creates a Mail box tied to an existing address in a world
     *
     * @param world   the world to create the mailbox in
     * @param address an existing Address,
     * @return whether the mailbox was created
     */
    boolean createMailBox(@Nonnull RegistryKey<World> world, @Nonnull UUID address);
}
