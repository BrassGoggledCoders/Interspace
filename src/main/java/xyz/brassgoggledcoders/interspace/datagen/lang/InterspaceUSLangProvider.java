package xyz.brassgoggledcoders.interspace.datagen.lang;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;

public class InterspaceUSLangProvider extends LanguageProvider {

    public InterspaceUSLangProvider(DataGenerator gen) {
        super(gen, Interspace.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(InterspaceBlocks.NAFASI.getPrimary(), "Block of Nafasi");
    }
}
