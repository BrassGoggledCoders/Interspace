package xyz.brassgoggledcoders.interspace;

import com.tterrag.registrate.Registrate;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;
import xyz.brassgoggledcoders.interspace.content.InterspaceItems;
import xyz.brassgoggledcoders.interspace.interspace.InterspaceStorage;

import javax.annotation.Nonnull;

@Mod(InterspaceMod.ID)
public class InterspaceMod {
    public static final String ID = "interspace";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    private static final NonNullLazy<Registrate> REGISTRATE = NonNullLazy.of(() -> Registrate.create(ID));

    public InterspaceMod() {

        InterspaceBlocks.setup();
        InterspaceItems.setup();

        InterspaceAPI.setStorageFunction(serverWorld -> serverWorld.getSavedData()
                .getOrCreate(InterspaceStorage::new, ID)
        );
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
