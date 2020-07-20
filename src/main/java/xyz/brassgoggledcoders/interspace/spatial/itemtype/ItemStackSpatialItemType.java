package xyz.brassgoggledcoders.interspace.spatial.itemtype;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItemType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ItemStackSpatialItemType extends SpatialItemType<ItemStack> {
    @Override
    @Nonnull
    public ItemStack fromSpacialItem(@Nonnull SpatialItem spatialItem) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(spatialItem.getRegistryName()));
        if (item != null) {
            ItemStack itemStack = new ItemStack(item, spatialItem.getCount());
            if (spatialItem.getNBT() != null) {
                itemStack.setTag(spatialItem.getNBT());
            }
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nullable
    public SpatialItem toSpacialItem(@Nonnull ItemStack object) {
        return new SpatialItem(this, Objects.requireNonNull(object.getItem().getRegistryName()).toString(),
                object.getCount(), object.getTag());
    }

    @Override
    public int getRetrievalSize() {
        return 64;
    }

    @Override
    public boolean matchesType(Object object) {
        return object instanceof ItemStack;
    }

    public Collection<SpatialItem> convert(Collection<ItemStack> generate) {
        Collection<SpatialItem> spatialItems = Lists.newArrayList();
        for (ItemStack itemStack: generate) {
            if (!itemStack.isEmpty()) {
                SpatialItem spatialItem = this.toSpacialItem(itemStack);
                if (spatialItem != null) {
                    spatialItems.add(spatialItem);
                }
            }
        }

        return spatialItems;
    }
}
