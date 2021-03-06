package xyz.brassgoggledcoders.interspace.block;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public enum ObeliskCoreState implements IStringSerializable {
    CONTROLLER,
    CONNECTED,
    INACTIVE;

    @Override
    @Nonnull
    public String getName() {
        return this.name().toLowerCase();
    }
}
