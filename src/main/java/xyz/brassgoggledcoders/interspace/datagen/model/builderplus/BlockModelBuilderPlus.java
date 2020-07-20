package xyz.brassgoggledcoders.interspace.datagen.model.builderplus;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Consumer;

public class BlockModelBuilderPlus extends BlockModelBuilder {

    public BlockModelBuilderPlus(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
        super(outputLocation, existingFileHelper);
    }

    public BlockModelBuilderPlus withElement(Consumer<ElementBuilderPlus<BlockModelBuilder>> element) {
        element.accept(new ElementBuilderPlus<>(this.element()));
        return this;
    }

    public BlockModelBuilderPlus withParent(ModelFile parent) {
        super.parent(parent);
        return this;
    }

    public BlockModelBuilderPlus withTexture(String key, String texture) {
        this.texture(key, texture);
        return this;
    }
}
