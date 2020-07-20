package xyz.brassgoggledcoders.interspace.datagen.tag;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;
import xyz.brassgoggledcoders.interspace.content.tag.InterspaceBlockTags;

import javax.annotation.Nonnull;

public class InterspaceBlockTagProvider extends BlockTagsProvider {
    public InterspaceBlockTagProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        this.getBuilder(InterspaceBlockTags.STORAGE_BLOCKS_NAFASI).add(InterspaceBlocks.NAFASI_BLOCK.getPrimary());
        this.getBuilder(Tags.Blocks.STORAGE_BLOCKS).add(InterspaceBlockTags.STORAGE_BLOCKS_NAFASI);

        this.getBuilder(InterspaceBlockTags.OBELISK).add(InterspaceBlocks.OBELISK_CORE.getPrimary());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Interspace Block Tags";
    }
}
