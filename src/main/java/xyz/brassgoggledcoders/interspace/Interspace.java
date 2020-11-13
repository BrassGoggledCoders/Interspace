package xyz.brassgoggledcoders.interspace;

import com.tterrag.registrate.Registrate;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialChunk;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialWorld;
import xyz.brassgoggledcoders.interspace.content.*;
import xyz.brassgoggledcoders.interspace.datagen.InterspaceDataGen;
import xyz.brassgoggledcoders.interspace.item.InterspaceItemGroup;
import xyz.brassgoggledcoders.interspace.json.SpatialEntryManager;
import xyz.brassgoggledcoders.interspace.nbt.EmptyNBTStorage;
import xyz.brassgoggledcoders.interspace.spatial.SpatialClient;

@Mod(Interspace.ID)
public class Interspace {
    public static final String ID = "interspace";
    public static final Logger LOGGER = LogManager.getLogger(ID);
    public static final NonNullLazy<ItemGroup> ITEM_GROUP = NonNullLazy.of(() -> new InterspaceItemGroup(ID,
            InterspaceItems.MIRROR::get));

    public static NonNullLazy<Registrate> REGISTRATE = NonNullLazy.of(() -> Registrate.create(ID)
            .itemGroup(ITEM_GROUP::get, "Interspace")
    );

    public final SpatialEntryManager spacialEntryManager;

    public static SpatialClient interspaceClient;

    public Interspace() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        InterspaceBlocks.setup();
        InterspaceItems.setup();
        InterspaceSpatialItemTypes.register(modEventBus);
        InterspaceSpatialTypes.register(modEventBus);
        InterspaceTileEntities.register(modEventBus);

        modEventBus.addListener(InterspaceDataGen::gatherData);
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.addListener(this::addReloadListener);

        spacialEntryManager = new SpatialEntryManager();

        InterspaceAPI.setSpacialEntryManager(spacialEntryManager);
        InterspaceAPI.setInterspaceClientSupplier(this::getInterspaceClient);
    }

    private SpatialClient getInterspaceClient() {
        return interspaceClient;
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(ISpatialWorld.class, new EmptyNBTStorage<>(), () -> null);
        CapabilityManager.INSTANCE.register(ISpatialChunk.class, new EmptyNBTStorage<>(), () -> null);
        CapabilityManager.INSTANCE.register(ISpatial.class, new EmptyNBTStorage<>(), () -> null);
    }

    private void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(spacialEntryManager);
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }

    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }
}
