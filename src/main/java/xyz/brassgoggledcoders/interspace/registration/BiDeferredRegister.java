package xyz.brassgoggledcoders.interspace.registration;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BiDeferredRegister<PRIMARY extends IForgeRegistryEntry<PRIMARY>, SECONDARY extends IForgeRegistryEntry<SECONDARY>> {
    public final DeferredRegister<PRIMARY> primaryRegister;
    public final DeferredRegister<SECONDARY> secondaryRegister;

    public BiDeferredRegister(String modid, IForgeRegistry<PRIMARY> primaryRegistry, IForgeRegistry<SECONDARY> secondaryRegistry) {
        primaryRegister = new DeferredRegister<>(primaryRegistry, modid);
        secondaryRegister = new DeferredRegister<>(secondaryRegistry, modid);
    }

    public <P extends PRIMARY, S extends SECONDARY, W extends BiRegistryObject<P, S>> W register(
            String name, Supplier<? extends P> primarySupplier, Supplier<? extends S> secondarySupplier,
            BiFunction<RegistryObject<P>, RegistryObject<S>, W> objectWrapper) {
        return objectWrapper.apply(primaryRegister.register(name, primarySupplier), secondaryRegister.register(name, secondarySupplier));
    }

    public <P extends PRIMARY, S extends SECONDARY, W extends BiRegistryObject<P, S>> W register(
            String name, Supplier<? extends P> primarySupplier, Function<P, S> secondarySupplier,
            BiFunction<RegistryObject<P>, RegistryObject<S>, W> objectWrapper) {
        RegistryObject<P> primaryObject = primaryRegister.register(name, primarySupplier);
        return objectWrapper.apply(primaryObject, secondaryRegister.register(name, () -> secondarySupplier.apply(primaryObject.get())));
    }

    public void register(IEventBus bus) {
        primaryRegister.register(bus);
        secondaryRegister.register(bus);
    }
}
