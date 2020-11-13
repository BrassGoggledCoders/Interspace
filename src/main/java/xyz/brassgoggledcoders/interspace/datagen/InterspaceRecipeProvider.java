package xyz.brassgoggledcoders.interspace.datagen;

import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;
import xyz.brassgoggledcoders.interspace.content.InterspaceItems;
import xyz.brassgoggledcoders.interspace.content.tag.InterspaceItemTags;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InterspaceRecipeProvider extends RecipeProvider {
    public InterspaceRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        this.decompress(InterspaceItems.NAFASI_INGOT, InterspaceItemTags.STORAGE_BLOCKS_NAFASI, consumer);
        this.decompress(InterspaceItems.NAFASI_NUGGET, InterspaceItemTags.INGOTS_NAFASI, consumer);

        this.compress(InterspaceBlocks.NAFASI, InterspaceItemTags.INGOTS_NAFASI, consumer);
        this.compress(InterspaceItems.NAFASI_INGOT, InterspaceItemTags.NUGGETS_NAFASI, consumer);
    }

    protected void decompress(Supplier<? extends IItemProvider> output, ITag<Item> input, Consumer<IFinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapelessRecipe(output.get(), 9)
                .addIngredient(input)
                .addCriterion("has_item", RecipeProvider.hasItem(input))
                .build(consumer, create(output, "decompress"));
    }

    protected void compress(Supplier<? extends IItemProvider> output, ITag<Item> input, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(output.get())
                .patternLine("###")
                .patternLine("###")
                .patternLine("###")
                .key('#', input)
                .addCriterion("has_item", RecipeProvider.hasItem(input))
                .build(consumer, create(output, "compress"));
    }

    protected ResourceLocation create(Supplier<? extends IItemProvider> output, String action) {
        return Interspace.rl(action + "/" + Objects.requireNonNull(output.get().asItem().getRegistryName())
                .getPath().replace("/", "_"));
    }
}
