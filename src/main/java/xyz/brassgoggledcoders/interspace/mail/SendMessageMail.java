package xyz.brassgoggledcoders.interspace.mail;

import net.minecraft.command.ICommandSource;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.mail.Mail;
import xyz.brassgoggledcoders.interspace.api.mail.MailType;
import xyz.brassgoggledcoders.interspace.content.InterspaceMailTypes;

public class SendMessageMail extends Mail {
    private ITextComponent message;
    private boolean shouldSave;

    public SendMessageMail(MailType type) {
        super(type);
    }

    public SendMessageMail(ITextComponent message) {
        this(message, true);
    }
    public SendMessageMail(ITextComponent message, boolean shouldSave) {
        this(InterspaceMailTypes.SEND_MESSAGE.get());
        this.message = message;
        this.shouldSave = shouldSave;
    }

    @Override
    public void receive(Object recipient) {
        if (recipient instanceof ICommandSource) {
            ((ICommandSource) recipient).sendMessage(message, InterspaceAPI.INTERSPACE_UUID);
        }
    }

    @Override
    public boolean shouldSave() {
        return shouldSave;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putString("message", ITextComponent.Serializer.toJson(message));
        nbt.putBoolean("shouldSave", shouldSave);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.message = ITextComponent.Serializer.getComponentFromJson(nbt.getString("message"));
        this.shouldSave = nbt.getBoolean("shouldSave");
    }
}
