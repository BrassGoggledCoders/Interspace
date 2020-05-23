package xyz.brassgoggledcoders.interspace.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import xyz.brassgoggledcoders.interspace.datagen.loot.InterspaceLootTableProvider;
import xyz.brassgoggledcoders.interspace.datagen.model.InterspaceBlockStateProvider;
import xyz.brassgoggledcoders.interspace.datagen.model.InterspaceItemModelProvider;
import xyz.brassgoggledcoders.interspace.datagen.tag.InterspaceBlockTagProvider;
import xyz.brassgoggledcoders.interspace.datagen.tag.InterspaceItemTagProvider;

public class InterspaceDataGen {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            dataGenerator.addProvider(new InterspaceBlockTagProvider(dataGenerator));
            dataGenerator.addProvider(new InterspaceItemTagProvider(dataGenerator));
            dataGenerator.addProvider(new InterspaceLootTableProvider(dataGenerator));
        }

        if (event.includeClient()) {
            BlockStateProvider blockStateProvider = new InterspaceBlockStateProvider(dataGenerator, existingFileHelper);
            dataGenerator.addProvider(blockStateProvider);
            dataGenerator.addProvider(new InterspaceItemModelProvider(dataGenerator, blockStateProvider.models().existingFileHelper));
        }
    }
}
