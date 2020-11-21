package xyz.brassgoggledcoders.interspace.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.item.InterspaceMirrorItem;

public class InterspaceItems {

    public static final RegistryEntry<InterspaceMirrorItem> INTERSPACE_MIRROR = InterspaceMod.getRegistrate()
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

    public static void setup() {

    }
}
