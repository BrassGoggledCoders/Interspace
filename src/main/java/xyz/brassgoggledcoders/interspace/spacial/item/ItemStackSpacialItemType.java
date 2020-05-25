package xyz.brassgoggledcoders.interspace.spacial.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItemType;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ItemStackSpacialItemType extends SpacialItemType<ItemStack> {
    @Override
    @Nonnull
    public ItemStack fromSpacialItem(@Nonnull SpacialItem spacialItem) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(spacialItem.getRegistryName()));
        if (item != null) {
            ItemStack itemStack = new ItemStack(item, spacialItem.getAmount());
            if (spacialItem.getData() != null) {
                itemStack.setTag(spacialItem.getData());
            }
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public SpacialItem toSpacialItem(@Nonnull ItemStack object) {
        return new SpacialItem(this, Objects.requireNonNull(object.getItem().getRegistryName()).toString(),
                object.getCount(), object.getTag());
    }

    @Override
    public boolean matchesType(Object object) {
        return object instanceof ItemStack;
    }
}
