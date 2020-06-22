package xyz.brassgoggledcoders.interspace.spatial.itemtype;

import com.google.common.collect.Lists;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItemType;
import xyz.brassgoggledcoders.interspace.fluid.ExpandingFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

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
    public int getRetrievalSize() {
        return 1000;
    }

    @Override
    public boolean matchesType(Object object) {
        return object instanceof FluidStack;
    }

    @Override
    public Collection<SpatialItem> convert(Collection<FluidStack> collection) {
        ExpandingFluidHandler fluidHandler = new ExpandingFluidHandler();
        for (FluidStack fluidStack: collection) {
            fluidHandler.fill(fluidStack, FluidAction.EXECUTE);
        }
        List<SpatialItem> spatialItems = Lists.newArrayList();
        for (FluidTank fluidTank: fluidHandler) {
            SpatialItem spatialItem = this.toSpacialItem(fluidTank.getFluid());
            if (spatialItem != null) {
                spatialItems.add(spatialItem);
            }
        }
        return spatialItems;
    }
}
