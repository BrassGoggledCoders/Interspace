package xyz.brassgoggledcoders.interspace.content;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;
import xyz.brassgoggledcoders.interspace.registration.LazyForgeRegistry;
import xyz.brassgoggledcoders.interspace.spatial.type.BasicCacheSpatialInstance;
import xyz.brassgoggledcoders.interspace.spatial.type.EmptySpatialInstance;
import xyz.brassgoggledcoders.interspace.spatial.type.StorageSpatialInstance;

@SuppressWarnings("unused")
public class InterspaceSpatialTypes {
    private static final DeferredRegister<SpatialType> SPACIAL_TYPE = new DeferredRegister<>(
            LazyForgeRegistry.<SpatialType>of(Interspace.rl("spacial_type")), Interspace.ID);

    public static final RegistryObject<SpatialType> EMPTY = SPACIAL_TYPE.register("empty", () ->
            new SpatialType(EmptySpatialInstance::new));
    public static final RegistryObject<SpatialType> BASIC_CACHE = SPACIAL_TYPE.register("basic_cache", () ->
            new SpatialType(BasicCacheSpatialInstance::new));
    public static final RegistryObject<SpatialType> STORAGE = SPACIAL_TYPE.register("storage", () ->
            new SpatialType(StorageSpatialInstance::new));

    public static void register(IEventBus modBus) {
        SPACIAL_TYPE.register(modBus);
    }
}
