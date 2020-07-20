package xyz.brassgoggledcoders.interspace.datagen.tag;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;
import xyz.brassgoggledcoders.interspace.content.InterspaceItems;
import xyz.brassgoggledcoders.interspace.content.tag.InterspaceItemTags;

import javax.annotation.Nonnull;

public class InterspaceItemTagProvider extends ItemTagsProvider {
    public InterspaceItemTagProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        this.getBuilder(InterspaceItemTags.INGOTS_NAFASI).add(InterspaceItems.NAFASI_INGOT.get());
        this.getBuilder(Tags.Items.INGOTS).add(InterspaceItemTags.INGOTS_NAFASI);

        this.getBuilder(InterspaceItemTags.NUGGETS_NAFASI).add(InterspaceItems.NAFASI_NUGGET.get());
        this.getBuilder(Tags.Items.NUGGETS).add(InterspaceItemTags.NUGGETS_NAFASI);

        this.getBuilder(InterspaceItemTags.STORAGE_BLOCKS_NAFASI).add(InterspaceBlocks.NAFASI_BLOCK.getSecondary());
        this.getBuilder(Tags.Items.STORAGE_BLOCKS).add(InterspaceItemTags.STORAGE_BLOCKS_NAFASI);

        this.getBuilder(InterspaceItemTags.OBELISK).add(InterspaceBlocks.OBELISK_CORE.getSecondary());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Interspace Item Tags";
    }
}
