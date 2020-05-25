package xyz.brassgoggledcoders.interspace;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspace;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItemType;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialType;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpacialItemTypes;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpacialTypes;
import xyz.brassgoggledcoders.interspace.datagen.InterspaceDataGen;
import xyz.brassgoggledcoders.interspace.nbt.EmptyNBTStorage;
import xyz.brassgoggledcoders.interspace.spacial.InterspaceClient;
import xyz.brassgoggledcoders.interspace.sql.DatabaseWrapper;

@Mod(Interspace.ID)
public class Interspace {
    public static final String ID = "interspace";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public Interspace() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        InterspaceBlocks.REGISTER.register(modEventBus);
        InterspaceSpacialItemTypes.register(modEventBus);
        InterspaceSpacialTypes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(InterspaceDataGen::gatherData);
        modEventBus.addListener(this::newRegistry);

        MinecraftForge.EVENT_BUS.addListener(DatabaseWrapper::handleServerStart);
        MinecraftForge.EVENT_BUS.addListener(DatabaseWrapper::handleServerStop);

        InterspaceAPI.setInterspaceClient(new InterspaceClient());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IInterspace.class, new EmptyNBTStorage<>(), () -> null);
    }

    @SuppressWarnings("unchecked")
    private void newRegistry(RegistryEvent.NewRegistry newRegistry) {
        makeRegistry("spacial_type", SpacialType.class);
        makeRegistry("spacial_item_type", SpacialItemType.class);
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
