package xyz.brassgoggledcoders.interspace.registration;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public class BiRegistryObject<PRIMARY extends IForgeRegistryEntry<? super PRIMARY>, SECONDARY extends IForgeRegistryEntry<? super SECONDARY>> {

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
}