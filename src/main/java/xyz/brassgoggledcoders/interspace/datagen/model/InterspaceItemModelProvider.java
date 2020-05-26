package xyz.brassgoggledcoders.interspace.datagen.model;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import xyz.brassgoggledcoders.interspace.Interspace;

import javax.annotation.Nonnull;

public class InterspaceItemModelProvider extends ItemModelProvider {
    public InterspaceItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Interspace.ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.withExistingParent("nafasi", modLoc("block/nafasi"));

        this.singleTexture("mirror", mcLoc("item/handheld"), modLoc("item/mirror"));
    }

    @Override
    @Nonnull
    public String getName() {
        return "Interspace Item Models";
    }
}
