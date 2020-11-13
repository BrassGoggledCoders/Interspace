package xyz.brassgoggledcoders.interspace.content.tag;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import xyz.brassgoggledcoders.interspace.Interspace;

public class InterspaceBlockTags {
    public static final IOptionalNamedTag<Block> STORAGE_BLOCKS_NAFASI = forgeTag("storage_blocks/nafasi");
    public static final IOptionalNamedTag<Block> OBELISK = interspaceTag("obelisk");

    public static IOptionalNamedTag<Block> forgeTag(String path) {
        return BlockTags.createOptional(new ResourceLocation("forge", path));
    }

    public static IOptionalNamedTag<Block> interspaceTag(String path) {
        return BlockTags.createOptional(Interspace.rl(path));
    }
}
