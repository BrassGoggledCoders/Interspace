package xyz.brassgoggledcoders.interspace.fluid;

import com.google.common.collect.Lists;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

public class ExpandingFluidHandler implements IFluidHandler, Iterable<FluidTank> {
    private final List<FluidTank> tanks;

    public ExpandingFluidHandler() {
        tanks = Lists.newArrayList();
    }

    @Override
    public int getTanks() {
        return tanks.size() + 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return tank < tanks.size() ? tanks.get(tank).getFluid() : FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank) {
        return tank < tanks.size() ? tanks.get(tank).getTankCapacity(0) : 64000;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return tank >= tanks.size() || tanks.get(tank).isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int currentAttempt = 0;
        int filled = 0;
        while(currentAttempt < tanks.size() && filled <= 0) {
            filled = tanks.get(currentAttempt).fill(resource, action);
            currentAttempt++;
        }

        if (filled <= 0) {
            if (action == FluidAction.EXECUTE) {
                FluidTank fluidTank = new FluidTank(64000);
                filled = fluidTank.fill(resource, action);
                if (filled > 0) {
                    tanks.add(fluidTank);
                }
            } else {
                filled = Math.min(resource.getAmount(), 64000);
            }
        }
        return filled;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack drained = FluidStack.EMPTY;
        int currentAttempt = 0;
        while (drained.isEmpty() && currentAttempt < tanks.size()) {
            drained = tanks.get(currentAttempt).drain(resource, action);
            currentAttempt++;
        }
        return drained;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack drained = FluidStack.EMPTY;
        int currentAttempt = 0;
        while (drained.isEmpty() && currentAttempt < tanks.size()) {
            drained = tanks.get(currentAttempt).drain(maxDrain, action);
            currentAttempt++;
        }
        return drained;
    }

    @Override
    @Nonnull
    public Iterator<FluidTank> iterator() {
        return tanks.iterator();
    }
}
