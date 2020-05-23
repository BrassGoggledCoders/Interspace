package xyz.brassgoggledcoders.interspace;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;
import xyz.brassgoggledcoders.interspace.datagen.InterspaceDataGen;

@Mod(Interspace.ID)
public class Interspace {
    public static final String ID = "interspace";

    public Interspace() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        InterspaceBlocks.REGISTER.register(modEventBus);

        modEventBus.addListener(InterspaceDataGen::gatherData);
    }


    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}
