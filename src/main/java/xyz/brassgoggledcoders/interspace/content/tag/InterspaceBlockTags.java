package xyz.brassgoggledcoders.interspace.content.tag;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.interspace.Interspace;

public class InterspaceBlockTags {
    public static final BlockTags.Wrapper STORAGE_BLOCKS_NAFASI = forgeTag("storage_blocks/nafasi");

    public static BlockTags.Wrapper forgeTag(String path) {
        return new BlockTags.Wrapper(new ResourceLocation("forge", path));
    }
}
