package xyz.brassgoggledcoders.interspace.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class CapabilityNBTStorage<T extends INBTSerializable<U>, U extends INBT> implements Capability.IStorage<T> {
    private final Class<U> nbtClass;

    public CapabilityNBTStorage(Class<U> nbtClass) {
        this.nbtClass = nbtClass;
    }

    @Nullable
    @Override
    public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
        if (nbtClass.isInstance(nbt)) {
            instance.deserializeNBT(nbtClass.cast(nbt));
        }
    }

    public static <V extends INBTSerializable<ListNBT>> CapabilityNBTStorage<V, ListNBT> listNBT() {
        return new CapabilityNBTStorage<>(ListNBT.class);
    }
}
