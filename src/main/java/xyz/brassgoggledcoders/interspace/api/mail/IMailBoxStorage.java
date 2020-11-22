package xyz.brassgoggledcoders.interspace.api.mail;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.UUID;

public interface IMailBoxStorage extends INBTSerializable<CompoundNBT> {
    @Nonnull
    Collection<Mail> getMail(@Nonnull UUID address);

    boolean sendMail(@Nonnull UUID address, @Nonnull Mail mail);

    @Nonnull
    UUID createMailBox();

    /**
     * Creates a Mailbox with a Specific address, useful for cases like Players where you can use their existing UUID
     * @param address the address to create a mailbox for
     * @return whether the mailbox was created (Generally means one already existed)
     */
    boolean createMailBox(UUID address);
}
