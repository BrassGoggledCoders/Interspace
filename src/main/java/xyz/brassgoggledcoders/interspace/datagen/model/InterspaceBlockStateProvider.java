package xyz.brassgoggledcoders.interspace.datagen.model;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;

import javax.annotation.Nonnull;

public class InterspaceBlockStateProvider extends BlockStateProvider {
    public InterspaceBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Interspace.ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(InterspaceBlocks.NAFASI.getPrimary());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Interspace BlockStates";
    }
}
