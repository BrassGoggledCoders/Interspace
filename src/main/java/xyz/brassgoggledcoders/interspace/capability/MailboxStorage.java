package xyz.brassgoggledcoders.interspace.capability;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.Constants.NBT;
import xyz.brassgoggledcoders.interspace.api.mail.IMailBoxStorage;
import xyz.brassgoggledcoders.interspace.api.mail.Mail;
import xyz.brassgoggledcoders.interspace.api.mail.MailBoxStatus;
import xyz.brassgoggledcoders.interspace.api.mail.MailType;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;

public class MailboxStorage implements IMailBoxStorage {
    private final Map<UUID, Queue<Mail>> mailBoxes;

    public MailboxStorage() {
        this.mailBoxes = Maps.newHashMap();
    }

    @Nonnull
    @Override
    public Collection<Mail> getMail(@Nonnull UUID address, int maxReceive) {
        Queue<Mail> mailBox = mailBoxes.get(address);
        if (mailBox != null) {
            if (!mailBox.isEmpty()) {
                int received = 0;
                Mail currentMail;
                List<Mail> gatheredMail = Lists.newArrayList();
                while (received < maxReceive && (currentMail = mailBox.poll()) != null) {
                    gatheredMail.add(currentMail);
                }
                return gatheredMail;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean sendMail(@Nonnull UUID address, @Nonnull Mail mail) {
        Queue<Mail> mailBox = mailBoxes.get(address);
        if (mailBox != null) {
            return mailBox.offer(mail);
        } else {
            return false;
        }
    }

    @Override
    public MailBoxStatus createMailBox(UUID address) {
        if (mailBoxes.containsKey(address)) {
            return MailBoxStatus.EXISTS;
        } else {
            mailBoxes.put(address, Queues.newPriorityQueue());
            return MailBoxStatus.CREATED;
        }
    }

    @Override
    public ListNBT serializeNBT() {
        ListNBT nbt = new ListNBT();
        for (Entry<UUID, Queue<Mail>> entry : mailBoxes.entrySet()) {
            CompoundNBT entryNBT = new CompoundNBT();
            entryNBT.putUniqueId("address", entry.getKey());
            ListNBT queueNBT = new ListNBT();
            for (Mail mail : entry.getValue()) {
                CompoundNBT mailNBT = MailType.serializeMail(mail);
                if (mailNBT != null) {
                    queueNBT.add(mailNBT);
                }
            }
            entryNBT.put("mailbox", queueNBT);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(ListNBT nbt) {
        mailBoxes.clear();
        for (int i = 0; i < nbt.size(); i++) {
            CompoundNBT entryNBT = nbt.getCompound(i);
            UUID address = entryNBT.getUniqueId("address");
            Queue<Mail> mailQueue = new PriorityQueue<>();
            ListNBT mailboxNBT = entryNBT.getList("mailbox", NBT.TAG_COMPOUND);
            for (int j = 0; j < mailboxNBT.size(); j++) {
                Mail mail = MailType.deserializeMail(mailboxNBT.getCompound(j));
                if (mail != null) {
                    mailQueue.offer(mail);
                }
            }
            mailBoxes.put(address, mailQueue);
        }
    }
}
