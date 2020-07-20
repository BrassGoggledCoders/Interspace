package xyz.brassgoggledcoders.interspace.datagen.model;

import com.google.common.base.Preconditions;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;
import xyz.brassgoggledcoders.interspace.datagen.model.builderplus.BlockModelBuilderPlus;

import javax.annotation.Nonnull;

public class InterspaceBlockStateProvider extends BlockStateProvider {
    public InterspaceBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Interspace.ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(InterspaceBlocks.NAFASI_BLOCK.getPrimary());
        this.simpleBlock(InterspaceBlocks.OBELISK_CORE.getPrimary());

        ModelFile baseSlate = this.getBuilderPlus("block/slate")
                .withParent(this.models().getExistingFile(mcLoc("block/block")))
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

        this.directionalBlock(InterspaceBlocks.QUERY_SLATE.getPrimary(),
                models().withExistingParent("query_slate", baseSlate.getLocation())
                        .texture("down", modLoc("block/nafasi_block"))
                        .texture("up", modLoc("block/query_slate_front"))
                        .texture("side", modLoc("block/nafasi_block"))
        );
    }

    public BlockModelBuilderPlus getBuilderPlus(String path) {
        Preconditions.checkNotNull(path, "Path must not be null");
        ResourceLocation outputLoc = extendWithFolder(path.contains(":") ? new ResourceLocation(path) : modLoc(path));
        return (BlockModelBuilderPlus) models().generatedModels.computeIfAbsent(outputLoc,
                loc -> new BlockModelBuilderPlus(loc, models().existingFileHelper));
    }

    private ResourceLocation extendWithFolder(ResourceLocation rl) {
        if (rl.getPath().contains("/")) {
            return rl;
        }
        return new ResourceLocation(rl.getNamespace(), "block/" + rl.getPath());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Interspace BlockStates";
    }
}
