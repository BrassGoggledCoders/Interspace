package xyz.brassgoggledcoders.interspace.datagen.tag;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;
import xyz.brassgoggledcoders.interspace.content.tag.InterspaceItemTags;

import javax.annotation.Nonnull;

public class InterspaceItemTagProvider extends ItemTagsProvider {
    public InterspaceItemTagProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        this.getBuilder(InterspaceItemTags.NAFASI)
                .add(InterspaceBlocks.NAFASI.getSecondary(), InterspaceBlocks.NAFASI.getSecondary());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Interspace Item Tags";
    }
}
