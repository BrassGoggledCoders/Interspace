package xyz.brassgoggledcoders.interspace.datagen.loot;

import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class InterspaceBlockLootTables extends BlockLootTables {
    @Override
    protected void addTables() {
        this.registerDropSelfLootTable(InterspaceBlocks.NAFASI_BLOCK.getPrimary());
        this.registerDropSelfLootTable(InterspaceBlocks.OBELISK_CORE.getPrimary());
        this.registerDropSelfLootTable(InterspaceBlocks.QUERY_SLATE.getPrimary());
    }

    @Override
    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        return InterspaceBlocks.REGISTER.primaryRegister
                .getEntries()
                .stream()
                .map(RegistryObject::get)
                .collect(Collectors.toList());
    }
}
