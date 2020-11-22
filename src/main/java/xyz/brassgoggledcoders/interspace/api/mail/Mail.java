package xyz.brassgoggledcoders.interspace.api.mail;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

public class Mail implements INBTSerializable<CompoundNBT>, Comparable<Mail> {
    private final MailType type;

    public Mail(MailType type) {
        this.type = type;
    }

    public MailType getType() {
        return type;
    }

    public void receive(Object recipient) {

    }

    public boolean shouldSave() {
        return true;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    public int getPriority() {
        return 0;
    }

    @Override
    public int compareTo(@Nonnull Mail otherMail) {
        return Integer.compare(this.getPriority(), otherMail.getPriority());
    }
}
