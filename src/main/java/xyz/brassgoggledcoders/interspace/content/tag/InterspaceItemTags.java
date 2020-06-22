package xyz.brassgoggledcoders.interspace.content.tag;

import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.interspace.Interspace;

public class InterspaceItemTags {

    public static final ItemTags.Wrapper INGOTS_NAFASI = forgeTag("ingots/nafasi");
    public static final ItemTags.Wrapper NUGGETS_NAFASI = forgeTag("nuggets/nafasi");
    public static final ItemTags.Wrapper STORAGE_BLOCKS_NAFASI = forgeTag("storage_blocks/nafasi");

    private static ItemTags.Wrapper forgeTag(String path) {
        return new ItemTags.Wrapper(new ResourceLocation("forge", path));
    }
}
