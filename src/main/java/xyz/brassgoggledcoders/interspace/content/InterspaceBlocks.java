package xyz.brassgoggledcoders.interspace.content;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.block.ObeliskCoreBlock;
import xyz.brassgoggledcoders.interspace.block.QuerySlateBlock;
import xyz.brassgoggledcoders.interspace.registration.BiRegistryObject;
import xyz.brassgoggledcoders.interspace.registration.BlockBiDeferredRegister;

public class InterspaceBlocks {
    public static final BlockBiDeferredRegister REGISTER = new BlockBiDeferredRegister(Interspace.ID,
            () -> new Item.Properties().group(Interspace.ITEM_GROUP));

    public static final BiRegistryObject<Block, BlockItem> NAFASI_BLOCK = REGISTER.register(
            "nafasi_block", () -> new Block(nafasiProperties())
    );

    public static final BiRegistryObject<ObeliskCoreBlock, BlockItem> OBELISK_CORE = REGISTER.register(
            "obelisk_core", () -> new ObeliskCoreBlock(nafasiProperties())
    );

    public static final BiRegistryObject<QuerySlateBlock, BlockItem> QUERY_SLATE = REGISTER.register(
            "query_slate", () -> new QuerySlateBlock(nafasiProperties())
    );

    private static Block.Properties nafasiProperties() {
        return Block.Properties.create(Material.IRON, MaterialColor.BLACK)
                .lightValue(1)
                .hardnessAndResistance(5.0F, 6.0F)
                .sound(SoundType.METAL);
    }
}
