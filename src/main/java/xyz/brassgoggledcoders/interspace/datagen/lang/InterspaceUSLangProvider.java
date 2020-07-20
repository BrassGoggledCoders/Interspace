package xyz.brassgoggledcoders.interspace.datagen.lang;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;
import xyz.brassgoggledcoders.interspace.content.*;

public class InterspaceUSLangProvider extends LanguageProvider {

    public InterspaceUSLangProvider(DataGenerator gen) {
        super(gen, Interspace.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(InterspaceBlocks.NAFASI_BLOCK.getPrimary(), "Block of Nafasi");
        this.add(InterspaceBlocks.OBELISK_CORE.getPrimary(), "Obelisk Core");
        this.add(InterspaceBlocks.QUERY_SLATE.getPrimary(), "Query Slate");

        this.add(InterspaceItems.MIRROR.get(), "Interspace Mirror");
        this.add(InterspaceItems.NAFASI_INGOT.get(), "Nafasi Ingot");
        this.add(InterspaceItems.NAFASI_NUGGET.get(), "Nafasi Nugget");

        this.add(InterspaceSpatialTypes.BASIC_CACHE.get(), "Basic Cache");
        this.add(InterspaceSpatialTypes.EMPTY.get(), "Nothingness");
        this.add(InterspaceSpatialTypes.STORAGE.get(), "Storage");
        this.add(InterspaceSpatialTypes.SHALLOW_SPRING.get(), "Shallow Spring");

        this.add("text.interspace.gaze", "You gaze into the Mirror and see: %s");
        this.add("text.interspace.nothing", "Nothingness");
        this.add("itemGroup.interspace", "Interspace");
    }

    protected void add(SpatialType spatialType, String message) {
        this.add(spatialType.getTranslationKey(), message);
    }

}
