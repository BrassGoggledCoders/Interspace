package xyz.brassgoggledcoders.interspace.datagen.lang;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialType;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;
import xyz.brassgoggledcoders.interspace.content.InterspaceItems;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpacialTypes;

public class InterspaceUSLangProvider extends LanguageProvider {

    public InterspaceUSLangProvider(DataGenerator gen) {
        super(gen, Interspace.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(InterspaceBlocks.NAFASI.getPrimary(), "Block of Nafasi");
        this.add(InterspaceBlocks.OBELISK_CORE.getPrimary(), "Obelisk Core");

        this.add(InterspaceItems.MIRROR.get(), "Interspace Mirror");

        this.add(InterspaceSpacialTypes.BASIC_CACHE.get(), "Basic Cache");
        this.add(InterspaceSpacialTypes.EMPTY.get(), "Nothingness");

        this.add("text.interspace.gaze", "You gaze into the Mirror and see: ");
        this.add("text.interspace.nothing", "Nothingness");
    }

    protected void add(SpacialType spacialType, String message) {
        this.add(spacialType.getTranslationKey(), message);
    }

}
