package xyz.brassgoggledcoders.interspace.registration;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class BlockBiDeferredRegister extends BiDeferredRegister<Block, Item> {
    private final Supplier<Item.Properties> defaultProperties;

    public BlockBiDeferredRegister(String modid, Supplier<Item.Properties> defaultProperties) {
        super(modid, ForgeRegistries.BLOCKS, ForgeRegistries.ITEMS);
        this.defaultProperties = defaultProperties;
    }

    public <BLOCK extends Block> BiRegistryObject<BLOCK, BlockItem> register(String name, Supplier<? extends BLOCK> blockSupplier) {
        return this.register(name, blockSupplier, BlockItem::new);
    }

    public <BLOCK extends Block, ITEM extends BlockItem> BiRegistryObject<BLOCK, ITEM> register(
            String name, Supplier<? extends BLOCK> blockSupplier, BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        return register(name, blockSupplier, block -> itemCreator.apply(block, defaultProperties.get()), BiRegistryObject::new);
    }
}
