package xyz.brassgoggledcoders.interspace.content;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.interspace.content.tag.InterspaceBlockTags;
import xyz.brassgoggledcoders.interspace.content.tag.InterspaceItemTags;
import xyz.brassgoggledcoders.interspace.datagen.model.builderplus.BlockModelBuilderPlus;

public class InterspaceAdditionData {

    public static void generateBlockModels(RegistrateBlockstateProvider blockstateProvider) {
        BlockModelBuilderPlus.create(blockstateProvider.models().getBuilder("block/slate"))
                .withParent(blockstateProvider.models().getExistingFile(blockstateProvider.mcLoc("block/block")))
                .withTexture("particle", "#side")
                .withElement(elementBuilder ->
                        elementBuilder.from(0, 0, 0)
                                .to(16, 4, 16)
                                .withFace(Direction.UP, faceBuilder ->
                                        faceBuilder.texture("#up")
                                                .uvs(0, 0, 16, 16)
                                                .cullface(Direction.UP)
                                )
                                .withFace(Direction.DOWN, faceBuilder ->
                                        faceBuilder.texture("#down")
                                                .uvs(0, 0, 16, 16)
                                                .cullface(Direction.DOWN)
                                )
                                .withFace(Direction.NORTH, faceBuilder ->
                                        faceBuilder.texture("#side")
                                                .uvs(0, 12, 16, 16)
                                                .cullface(Direction.NORTH)
                                )
                                .withFace(Direction.SOUTH, faceBuilder ->
                                        faceBuilder.texture("#side")
                                                .uvs(0, 12, 16, 16)
                                                .cullface(Direction.SOUTH)
                                )
                                .withFace(Direction.EAST, faceBuilder ->
                                        faceBuilder.texture("#side")
                                                .uvs(0, 12, 16, 16)
                                                .cullface(Direction.EAST)
                                )
                                .withFace(Direction.WEST, faceBuilder ->
                                        faceBuilder.texture("#side")
                                                .uvs(0, 12, 16, 16)
                                                .cullface(Direction.WEST)
                                )
                );
    }

    public static void generateBlockTags(RegistrateTagsProvider<Block> tagsProvider) {
        tagsProvider.getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS)
                .addTag(InterspaceBlockTags.STORAGE_BLOCKS_NAFASI);
    }

    public static void generateItemTags(RegistrateTagsProvider<Item> tagsProvider) {
        tagsProvider.getOrCreateBuilder(Tags.Items.INGOTS).addTag(InterspaceItemTags.INGOTS_NAFASI);
        tagsProvider.getOrCreateBuilder(Tags.Items.NUGGETS).addTag(InterspaceItemTags.NUGGETS_NAFASI);
        tagsProvider.getOrCreateBuilder(Tags.Items.STORAGE_BLOCKS).addTag(InterspaceItemTags.STORAGE_BLOCKS_NAFASI);
    }
}
