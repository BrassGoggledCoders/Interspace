package xyz.brassgoggledcoders.interspace.datagen.lang;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;
import xyz.brassgoggledcoders.interspace.content.InterspaceBlocks;
import xyz.brassgoggledcoders.interspace.content.InterspaceEntities;
import xyz.brassgoggledcoders.interspace.content.InterspaceItems;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpatialTypes;

public class InterspaceUSLangProvider extends LanguageProvider {

    public InterspaceUSLangProvider(DataGenerator gen) {
        super(gen, Interspace.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(InterspaceBlocks.NAFASI.getPrimary(), "Block of Nafasi");
        this.add(InterspaceBlocks.OBELISK_CORE.getPrimary(), "Obelisk Core");

        this.add(InterspaceItems.MIRROR.get(), "Interspace Mirror");
        this.add(InterspaceItems.QUERY_SLATE.get(), "Interspace Query Slate");

        this.add(InterspaceEntities.QUERY_SLATE.get(), "Interspace Query Slate");

        this.add(InterspaceSpatialTypes.BASIC_CACHE.get(), "Basic Cache");
        this.add(InterspaceSpatialTypes.EMPTY.get(), "Nothingness");

        this.add("text.interspace.gaze", "You gaze into the Mirror and see: %s");
        this.add("text.interspace.nothing", "Nothingness");
        this.add("itemGroup.interspace", "Interspace");
    }

    protected void add(SpatialType spatialType, String message) {
        this.add(spatialType.getTranslationKey(), message);
    }

}
