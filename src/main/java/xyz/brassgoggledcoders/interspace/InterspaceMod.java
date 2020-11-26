package xyz.brassgoggledcoders.interspace;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.interspace.api.mail.IMailBoxStorage;
import xyz.brassgoggledcoders.interspace.api.mail.MailType;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.capability.CapabilityNBTStorage;
import xyz.brassgoggledcoders.interspace.config.InterspaceServerConfig;
import xyz.brassgoggledcoders.interspace.content.*;
import xyz.brassgoggledcoders.interspace.capability.MailboxStorage;

import javax.annotation.Nonnull;

@Mod(InterspaceMod.ID)
public class InterspaceMod {
    public static final String ID = "interspace";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    private static final Pair<InterspaceServerConfig, ForgeConfigSpec> SERVER_CONFIG = new ForgeConfigSpec.Builder()
            .configure(InterspaceServerConfig::new);

    private static final NonNullLazy<Registrate> REGISTRATE = NonNullLazy.of(() -> Registrate.create(ID)
            .addDataGenerator(ProviderType.LANG, InterspaceAdditionalData::generateText)
            .itemGroup(() -> new ItemGroup(ID) {
                @Override
                @Nonnull
                public ItemStack createIcon() {
                    return InterspaceItems.INTERSPACE_MIRROR
                            .map(ItemStack::new)
                            .orElse(ItemStack.EMPTY);
                }
            }, "Interspace")
    );

    public InterspaceMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::commonSetup);

        new RegistryBuilder<MailType>()
                .setType(MailType.class)
                .setName(rl("mail_type"))
                .create();

        new RegistryBuilder<TaskType>()
                .setType(TaskType.class)
                .setName(rl("task_type"))
                .create();

        InterspaceBlocks.setup();
        InterspaceItems.setup();
        InterspaceTaskTypes.setup();
        InterspaceMailTypes.setup();
        InterspaceFeatures.setup();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG.getRight());
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IMailBoxStorage.class, CapabilityNBTStorage.listNBT(), MailboxStorage::new);
    }

    @Nonnull
    public static ResourceLocation rl(@Nonnull String path) {
        return new ResourceLocation(ID, path);
    }

    @Nonnull
    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }

    @Nonnull
    public static InterspaceServerConfig getServerConfig() {
        return SERVER_CONFIG.getLeft();
    }
}
