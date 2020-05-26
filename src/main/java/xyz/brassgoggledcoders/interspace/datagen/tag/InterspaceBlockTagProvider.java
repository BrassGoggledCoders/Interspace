package xyz.brassgoggledcoders.interspace.datagen.tag;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import xyz.brassgoggledcoders.interspace.content.tag.InterspaceBlockTags;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;

import javax.annotation.Nonnull;

public class InterspaceBlockTagProvider extends BlockTagsProvider {
    public InterspaceBlockTagProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        this.getBuilder(InterspaceBlockTags.NAFASI)
                .add(InterspaceBlocks.NAFASI.getPrimary(), InterspaceBlocks.OBELISK_CORE.getPrimary());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Interspace Block Tags";
    }
}
