package xyz.brassgoggledcoders.interspace.mail;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundNBT;
import xyz.brassgoggledcoders.interspace.api.mail.IMailBoxStorage;
import xyz.brassgoggledcoders.interspace.api.mail.Mail;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class MailboxStorage implements IMailBoxStorage {
    private final Map<UUID, MailBox> mailBoxes;

    public MailboxStorage() {
        this.mailBoxes = Maps.newHashMap();
    }

    @Nonnull
    @Override
    public Collection<Mail> getMail(@Nonnull UUID address) {
        MailBox mailBox = mailBoxes.get(address);
        if (mailBox != null) {
            return mailBox.getMail();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean sendMail(@Nonnull UUID address, @Nonnull Mail mail) {
        return false;
    }

    @Nonnull
    @Override
    public UUID createMailBox() {
        UUID address = UUID.randomUUID();
        mailBoxes.put(address, new MailBox());
        return address;
    }

    @Override
    public boolean createMailBox(UUID address) {
        if (mailBoxes.containsKey(address)) {
            return false;
        } else {
            mailBoxes.put(address, new MailBox());
            return true;
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
