package xyz.brassgoggledcoders.interspace.content;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.client.model.generators.ModelFile;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.block.ObeliskCoreBlock;
import xyz.brassgoggledcoders.interspace.block.QuerySlateBlock;
import xyz.brassgoggledcoders.interspace.content.tag.InterspaceBlockTags;
import xyz.brassgoggledcoders.interspace.content.tag.InterspaceItemTags;

public class InterspaceBlocks {

    public static final BlockEntry<Block> NAFASI = Interspace.getRegistrate()
            .object("nafasi")
            .block(Block::new)
            .initialProperties(Material.IRON, MaterialColor.BLACK)
            .properties(InterspaceBlocks::nafasiProperties)
            .tag(InterspaceBlockTags.STORAGE_BLOCKS_NAFASI)
            .lang("Block of Nafasi")
            .item()
            .tag(InterspaceItemTags.STORAGE_BLOCKS_NAFASI)
            .build()
            .register();

    public static final BlockEntry<ObeliskCoreBlock> OBELISK_CORE = Interspace.getRegistrate()
            .object("obelisk_core")
            .block(ObeliskCoreBlock::new)
            .initialProperties(Material.IRON, MaterialColor.BLACK)
            .properties(InterspaceBlocks::nafasiProperties)
            .tag(InterspaceBlockTags.OBELISK)
            .lang("Obelisk Core")
            .blockstate((context, provider) -> provider.directionalBlock(context.get(), provider.models()
                    .getBuilder("query_slate")
                    .parent(new ModelFile.UncheckedModelFile(Interspace.rl("slate")))
                    .texture("down", provider.modLoc("block/nafasi_block"))
                    .texture("up", provider.modLoc("block/query_slate_front"))
                    .texture("side", provider.modLoc("block/nafasi_block"))
            ))
            .item()
            .tag(InterspaceItemTags.OBELISK)
            .build()
            .register();

    public static final BlockEntry<QuerySlateBlock> QUERY_SLATE = Interspace.getRegistrate()
            .object("query_slate")
            .block(QuerySlateBlock::new)
            .initialProperties(Material.IRON, MaterialColor.BLACK)
            .properties(InterspaceBlocks::nafasiProperties)
            .lang("Query Slate")
            .simpleItem()
            .register();

    private static AbstractBlock.Properties nafasiProperties(AbstractBlock.Properties properties) {
        return properties.setLightLevel(blockState -> 1)
                .hardnessAndResistance(5.0F, 6.0F)
                .sound(SoundType.METAL);
    }

    public static void setup() {

    }
}
