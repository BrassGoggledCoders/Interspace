package xyz.brassgoggledcoders.interspace.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import xyz.brassgoggledcoders.interspace.api.mail.IMailBoxStorage;

public class InterspaceCapabilities {
    @CapabilityInject(IMailBoxStorage.class)
    public static Capability<IMailBoxStorage> MAILBOXES;
}
