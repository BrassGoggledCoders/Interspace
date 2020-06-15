package xyz.brassgoggledcoders.interspace.spatial.item;

import com.google.common.collect.Lists;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItemType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class FluidStackSpatialItemType extends SpatialItemType<FluidStack> {
    @Override
    @Nonnull
    public FluidStack fromSpacialItem(@Nonnull SpatialItem spatialItem) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(spatialItem.getRegistryName()));
        if (fluid != null) {
            FluidStack fluidStack = new FluidStack(fluid, spatialItem.getCount());
            if (spatialItem.getNBT() != null) {
                fluidStack.setTag(spatialItem.getNBT());
            }
            return fluidStack;
        }
        return FluidStack.EMPTY;
    }

    @Override
    @Nullable
    public SpatialItem toSpacialItem(@Nonnull FluidStack object) {
        if (!object.isEmpty()) {
            return new SpatialItem(this, Objects.requireNonNull(object.getFluid().getRegistryName()).toString(),
                    object.getAmount(), object.getTag());
        } else {
            return null;
        }
    }

    @Override
    public boolean matchesType(Object object) {
        return object instanceof FluidStack;
    }

    @Override
    public Collection<SpatialItem> convert(Collection<FluidStack> collection) {
        List<SpatialItem> spatialItems = Lists.newArrayList();
        for (FluidStack fluidStack: collection) {
            SpatialItem spatialItem = this.toSpacialItem(fluidStack);
            if (spatialItem != null) {
                spatialItems.add(spatialItem);
            }
        }
        return spatialItems;
    }
}
