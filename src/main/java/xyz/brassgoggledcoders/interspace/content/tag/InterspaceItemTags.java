package xyz.brassgoggledcoders.interspace.content.tag;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import xyz.brassgoggledcoders.interspace.Interspace;

public class InterspaceItemTags {

    public static final IOptionalNamedTag<Item> INGOTS_NAFASI = forgeTag("ingots/nafasi");
    public static final IOptionalNamedTag<Item> NUGGETS_NAFASI = forgeTag("nuggets/nafasi");
    public static final IOptionalNamedTag<Item> STORAGE_BLOCKS_NAFASI = forgeTag("storage_blocks/nafasi");
    public static final IOptionalNamedTag<Item> OBELISK = interspaceTag("obelisk");

    private static IOptionalNamedTag<Item> forgeTag(String path) {
        return ItemTags.createOptional(new ResourceLocation("forge", path));
    }

    private static IOptionalNamedTag<Item> interspaceTag(String path) {
        return ItemTags.createOptional(Interspace.rl(path));
    }
}
