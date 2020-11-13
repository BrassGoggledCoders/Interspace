package xyz.brassgoggledcoders.interspace.datagen.model.builderplus;

import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Consumer;

public class BlockModelBuilderPlus {
    private final BlockModelBuilder blockModelBuilder;

    public BlockModelBuilderPlus(BlockModelBuilder blockModelBuilder) {
        this.blockModelBuilder = blockModelBuilder;
    }

    public BlockModelBuilderPlus withElement(Consumer<ElementBuilderPlus<BlockModelBuilder>> element) {
        element.accept(new ElementBuilderPlus<>(blockModelBuilder.element()));
        return this;
    }

    public BlockModelBuilderPlus withParent(ModelFile parent) {
        blockModelBuilder.parent(parent);
        return this;
    }

    public BlockModelBuilderPlus withTexture(String key, String texture) {
        blockModelBuilder.texture(key, texture);
        return this;
    }

    public ModelFile getModel() {
        return blockModelBuilder;
    }


    public static BlockModelBuilderPlus create(BlockModelBuilder builder) {
        return new BlockModelBuilderPlus(builder);
    }
}
