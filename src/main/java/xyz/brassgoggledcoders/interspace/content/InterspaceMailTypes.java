package xyz.brassgoggledcoders.interspace.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.mail.MailType;
import xyz.brassgoggledcoders.interspace.mail.SendMessageMail;
import xyz.brassgoggledcoders.interspace.manager.InterspaceManager;

public class InterspaceMailTypes {
    public static final RegistryEntry<MailType> SEND_MESSAGE = InterspaceMod.getRegistrate()
            .object("send_message")
            .simple(MailType.class, () -> MailType.of(SendMessageMail::new));
    public static void setup() {

    }
}
