package xyz.brassgoggledcoders.interspace;

import com.tterrag.registrate.Registrate;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.source.SourceType;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;
import xyz.brassgoggledcoders.interspace.content.InterspaceItems;
import xyz.brassgoggledcoders.interspace.content.InterspaceSourceTypes;
import xyz.brassgoggledcoders.interspace.content.InterspaceTaskTypes;
import xyz.brassgoggledcoders.interspace.manager.InterspaceManager;

import javax.annotation.Nonnull;

@Mod(InterspaceMod.ID)
public class InterspaceMod {
    public static final String ID = "interspace";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    private static final NonNullLazy<Registrate> REGISTRATE = NonNullLazy.of(() -> Registrate.create(ID));

    public InterspaceMod() {
        InterspaceAPI.setManager(InterspaceManager.INSTANCE);

        new RegistryBuilder<TaskType>()
                .setType(TaskType.class)
                .setName(rl("task_type"))
                .create();

        new RegistryBuilder<SourceType>()
                .setType(SourceType.class)
                .setName(rl("source_type"))
                .create();

        InterspaceBlocks.setup();
        InterspaceItems.setup();
        InterspaceSourceTypes.setup();
        InterspaceTaskTypes.setup();
    }


    @Nonnull
    public static ResourceLocation rl(@Nonnull String path) {
        return new ResourceLocation(ID, path);
    }

    @Nonnull
    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }
}
