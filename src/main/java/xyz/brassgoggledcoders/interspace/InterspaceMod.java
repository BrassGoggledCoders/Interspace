package xyz.brassgoggledcoders.interspace;

import com.tterrag.registrate.Registrate;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.config.InterspaceServerConfig;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;
import xyz.brassgoggledcoders.interspace.content.InterspaceItems;
import xyz.brassgoggledcoders.interspace.content.InterspaceTaskTypes;
import xyz.brassgoggledcoders.interspace.manager.InterspaceManager;

import javax.annotation.Nonnull;

@Mod(InterspaceMod.ID)
public class InterspaceMod {
    public static final String ID = "interspace";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    private static final Pair<InterspaceServerConfig, ForgeConfigSpec> SERVER_CONFIG = new ForgeConfigSpec.Builder()
            .configure(InterspaceServerConfig::new);

    private static final NonNullLazy<Registrate> REGISTRATE = NonNullLazy.of(() -> Registrate.create(ID)
            .itemGroup(() -> new ItemGroup(ID) {
                @Override
                @Nonnull
                public ItemStack createIcon() {
                    return InterspaceItems.INTERSPACE_MIRROR
                            .map(ItemStack::new)
                            .orElse(ItemStack.EMPTY);
                }
            })
    );

    public InterspaceMod() {
        InterspaceAPI.setManager(InterspaceManager.INSTANCE);

        new RegistryBuilder<TaskType>()
                .setType(TaskType.class)
                .setName(rl("task_type"))
                .create();

        InterspaceBlocks.setup();
        InterspaceItems.setup();
        InterspaceTaskTypes.setup();


        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG.getRight());
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
