package xyz.brassgoggledcoders.interspace;

import com.hrznstudio.titanium.tab.TitaniumTab;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialChunk;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialWorld;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItemType;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;
import xyz.brassgoggledcoders.interspace.content.*;
import xyz.brassgoggledcoders.interspace.datagen.InterspaceDataGen;
import xyz.brassgoggledcoders.interspace.json.SpatialEntryManager;
import xyz.brassgoggledcoders.interspace.nbt.EmptyNBTStorage;
import xyz.brassgoggledcoders.interspace.spatial.SpatialClient;

@Mod(Interspace.ID)
public class Interspace {
    public static final String ID = "interspace";
    public static final Logger LOGGER = LogManager.getLogger(ID);
    public static final ItemGroup ITEM_GROUP = new TitaniumTab(ID, InterspaceItems.MIRROR.lazyMap(ItemStack::new));

    public final SpatialEntryManager spacialEntryManager;

    public static SpatialClient interspaceClient;

    public Interspace() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        InterspaceBlocks.REGISTER.register(modEventBus);
        InterspaceItems.register(modEventBus);
        InterspaceSpatialItemTypes.register(modEventBus);
        InterspaceSpatialTypes.register(modEventBus);
        InterspaceEntities.register(modEventBus);

        modEventBus.addListener(InterspaceDataGen::gatherData);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::newRegistry);
        modEventBus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);

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
    }

    private void clientSetup(FMLClientSetupEvent event) {
        EntityRendererManager rendererManager = event.getMinecraftSupplier()
                .get()
                .getRenderManager();
        ItemRenderer itemRenderer = event.getMinecraftSupplier()
                .get()
                .getItemRenderer();

        rendererManager.register(InterspaceEntities.QUERY_SLATE.get(), new ItemFrameRenderer(rendererManager, itemRenderer));
    }

    private void serverAboutToStart(FMLServerAboutToStartEvent event) {
        event.getServer().getResourceManager().addReloadListener(spacialEntryManager);
    }

    @SuppressWarnings("unchecked")
    private void newRegistry(RegistryEvent.NewRegistry newRegistry) {
        makeRegistry("spacial_type", SpatialType.class);
        makeRegistry("spacial_item_type", SpatialItemType.class);
    }

    private static <T extends IForgeRegistryEntry<T>> void makeRegistry(String name, Class<T> type) {
        new RegistryBuilder<T>()
                .setName(Interspace.rl(name))
                .setType(type)
                .create();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}
