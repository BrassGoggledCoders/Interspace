package xyz.brassgoggledcoders.interspace.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import xyz.brassgoggledcoders.interspace.datagen.lang.InterspaceUSLangProvider;
import xyz.brassgoggledcoders.interspace.datagen.loot.InterspaceLootTableProvider;
import xyz.brassgoggledcoders.interspace.datagen.spatial.InterspaceSpatialProvider;

public class InterspaceDataGen {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();

        if (event.includeServer()) {
            dataGenerator.addProvider(new InterspaceLootTableProvider(dataGenerator));
            dataGenerator.addProvider(new InterspaceSpatialProvider(dataGenerator));
            dataGenerator.addProvider(new InterspaceRecipeProvider(dataGenerator));
        }

        if (event.includeClient()) {
            dataGenerator.addProvider(new InterspaceUSLangProvider(dataGenerator));
        }
    }
}
