package xyz.brassgoggledcoders.interspace.mail;

import com.google.common.collect.Lists;
import xyz.brassgoggledcoders.interspace.api.mail.Mail;

import java.util.Collection;
import java.util.List;

public class MailBox {
    private final List<Mail> mail;

    public MailBox() {
        this.mail = Lists.newArrayList();
    }

    public Collection<Mail> getMail() {
        return mail;
    }
}
