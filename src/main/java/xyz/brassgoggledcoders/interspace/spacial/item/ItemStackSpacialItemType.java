package xyz.brassgoggledcoders.interspace.spacial.item;

import com.google.common.collect.Lists;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItemType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ItemStackSpacialItemType extends SpacialItemType<ItemStack> {
    @Override
    @Nonnull
    public ItemStack fromSpacialItem(@Nonnull SpacialItem spacialItem) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(spacialItem.getRegistryName()));
        if (item != null) {
            ItemStack itemStack = new ItemStack(item, spacialItem.getCount());
            if (spacialItem.getNBT() != null) {
                itemStack.setTag(spacialItem.getNBT());
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

    public Collection<SpacialItem> convertInventory(IItemHandler handler) {
        List<SpacialItem> spacialItems = Lists.newArrayList();
        for (int slotNum = 0; slotNum < handler.getSlots(); slotNum++) {
            ItemStack itemStack = handler.extractItem(slotNum, 64, false);
            if (!itemStack.isEmpty()) {
                spacialItems.add(this.toSpacialItem(itemStack));
            }
        }
        return spacialItems;
    }

    public Collection<SpacialItem> convertCollection(Collection<ItemStack> generate) {
        ItemStackHandler handler = new ItemStackHandler(generate.size());
        generate.forEach(itemStack -> ItemHandlerHelper.insertItemStacked(handler, itemStack, false));
        return convertInventory(handler);
    }
}
