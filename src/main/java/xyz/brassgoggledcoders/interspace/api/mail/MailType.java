package xyz.brassgoggledcoders.interspace.api.mail;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.interspace.api.InterspaceRegistries;

import java.util.Objects;
import java.util.function.Function;

public class MailType extends ForgeRegistryEntry<MailType> {
    private final Function<MailType, Mail> taskCreator;

    public MailType(Function<MailType, Mail> taskCreator) {
        this.taskCreator = taskCreator;
    }

    public Mail create() {
        return this.taskCreator.apply(this);
    }

    public static MailType of(Function<MailType, Mail> mailCreator) {
        return new MailType(mailCreator);
    }

    public static Mail deserializeMail(CompoundNBT nbt) {
        MailType type = InterspaceRegistries.MAIL_TYPE.getValue(new ResourceLocation(nbt.getString("type")));
        if (type != null) {
            Mail mail = type.create();
            mail.deserializeNBT(nbt);
            return mail;
        }
        return null;
    }

    public static CompoundNBT serializeMail(Mail mail) {
        if (mail.shouldSave()) {
            CompoundNBT nbt = mail.serializeNBT();
            nbt.putString("type", Objects.requireNonNull(mail.getType().getRegistryName()).toString());
            return nbt;
        } else {
            return null;
        }
    }
}

