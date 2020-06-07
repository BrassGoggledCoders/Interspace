package xyz.brassgoggledcoders.interspace.api.spacial.item;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;
import java.util.Objects;

public class SpacialItem {
    private final SpacialItemType<?> type;
    private final String registryName;
    private final int amount;
    private final CompoundNBT data;

    public SpacialItem(SpacialItemType<?> type, String registryName, int amount, @Nullable CompoundNBT data) {
        this.type = type;
        this.registryName = registryName;
        this.amount = amount;
        this.data = data;
    }

    public String getRegistryName() {
        return registryName;
    }

    public int getCount() {
        return amount;
    }

    @Nullable
    public CompoundNBT getNBT() {
        return data;
    }

    public SpacialItemType<?> getType() {
        return type;
    }

    public String getTypeString() {
        return Objects.requireNonNull(this.getType().getRegistryName()).toString();
    }
}
