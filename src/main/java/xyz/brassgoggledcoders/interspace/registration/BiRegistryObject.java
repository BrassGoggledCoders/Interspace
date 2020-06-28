package xyz.brassgoggledcoders.interspace.registration;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class BiRegistryObject<PRIMARY extends IForgeRegistryEntry<? super PRIMARY>, SECONDARY extends IForgeRegistryEntry<? super SECONDARY>> implements Supplier<PRIMARY> {

    private final RegistryObject<PRIMARY> primaryRO;
    private final RegistryObject<SECONDARY> secondaryRO;

    public BiRegistryObject(RegistryObject<PRIMARY> primaryRO, RegistryObject<SECONDARY> secondaryRO) {
        this.primaryRO = primaryRO;
        this.secondaryRO = secondaryRO;
    }

    @Nonnull
    public PRIMARY getPrimary() {
        return primaryRO.get();
    }

    @Nonnull
    public SECONDARY getSecondary() {
        return secondaryRO.get();
    }

    @Override
    public PRIMARY get() {
        return this.getPrimary();
    }

    public boolean matches(Object primary) {
        return this.primaryRO.isPresent() && this.primaryRO.get() == primary;
    }
}