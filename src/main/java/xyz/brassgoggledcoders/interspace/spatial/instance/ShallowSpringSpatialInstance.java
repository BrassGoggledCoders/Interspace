package xyz.brassgoggledcoders.interspace.spatial.instance;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpatialItemTypes;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ShallowSpringSpatialInstance extends LootTableSpatialInstance {
    public ShallowSpringSpatialInstance(SpatialType spatialType, IWorld world, ChunkPos chunkPos) {
        super(spatialType, world, chunkPos);
    }

    @Override
    protected Collection<SpatialItem> convertEntries(List<ItemStack> lootList) {
        List<FluidStack> fluidStacks = Lists.newArrayList();
        for (ItemStack itemStack : lootList) {
            itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                    .map(handler -> handler.drain(1000000, IFluidHandler.FluidAction.EXECUTE))
                    .filter(fluidStack -> !fluidStack.isEmpty())
                    .ifPresent(fluidStacks::add);
        }
        return InterspaceSpatialItemTypes.FLUID_STACK
                .map(type -> type.convert(fluidStacks))
                .orElseGet(Collections::emptyList);
    }
}
