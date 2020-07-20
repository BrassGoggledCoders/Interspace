package xyz.brassgoggledcoders.interspace.datagen.model;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;
import xyz.brassgoggledcoders.interspace.content.InterspaceItems;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Supplier;

public class InterspaceItemModelProvider extends ItemModelProvider {
    public InterspaceItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Interspace.ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.blockItem(InterspaceBlocks.NAFASI_BLOCK);
        this.blockItem(InterspaceBlocks.OBELISK_CORE);
        this.blockItem(InterspaceBlocks.QUERY_SLATE);

        this.singleTexture("mirror", mcLoc("item/handheld"), "layer0", modLoc("item/mirror"));

        this.genericItem(InterspaceItems.NAFASI_INGOT);
        this.genericItem(InterspaceItems.NAFASI_NUGGET);
    }

    protected ItemModelBuilder blockItem(Supplier<? extends Block> block) {
        return Optional.ofNullable(block.get())
                .map(Block::asItem)
                .map(Item::getRegistryName)
                .map(ResourceLocation::getPath)
                .map(path -> this.withExistingParent(path, modLoc("block/" + path)))
                .orElseThrow(() -> new IllegalStateException("Failed to create model for Block Item"));
    }

    protected ItemModelBuilder genericItem(Supplier<? extends Item> item) {
        return Optional.ofNullable(item.get())
                .map(Item::getRegistryName)
                .map(ResourceLocation::getPath)
                .map(this::genericItem)
                .orElseThrow(() -> new IllegalStateException("Tried to generate model for invalid Item"));
    }

    protected ItemModelBuilder genericItem(String path) {
        return this.singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path));
    }

    @Override
    @Nonnull
    public String getName() {
        return "Interspace Item Models";
    }
}
