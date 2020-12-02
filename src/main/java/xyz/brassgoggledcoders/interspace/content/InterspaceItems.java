package xyz.brassgoggledcoders.interspace.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegistryEvent;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.item.InterspaceMirrorItem;

public class InterspaceItems {

    public static final ItemEntry<InterspaceMirrorItem> INTERSPACE_MIRROR = InterspaceMod.getRegistrate()
            .object("interspace_mirror")
            .item(InterspaceMirrorItem::new)
            .recipe((context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get())
                    .patternLine("GMG")
                    .patternLine("PEP")
                    .patternLine("GMG")
                    .key('G', Tags.Items.INGOTS_GOLD)
                    .key('M', Tags.Items.GLASS)
                    .key('P', ItemTags.PLANKS)
                    .key('E', Tags.Items.ENDER_PEARLS)
                    .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Tags.Items.ENDER_PEARLS))
                    .build(provider)
            )
            .register();

    public static final ItemEntry<Item> NAFASI_NUGGET = InterspaceMod.getRegistrate()
            .object("nafasi_nugget")
            .item(Item::new)
            .lang("Nafasi Nugget")
            .register();

    public static void setup() {

    }
}
