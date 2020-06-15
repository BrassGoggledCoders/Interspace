package xyz.brassgoggledcoders.interspace.datagen.patchouli;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IDataProvider;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.patchouliprovider.BookBuilder;
import xyz.brassgoggledcoders.patchouliprovider.PatchouliBookProvider;

import java.util.function.Consumer;

public class InterspaceBookProvider extends PatchouliBookProvider implements IDataProvider {
    public InterspaceBookProvider(DataGenerator gen) {
        super(gen, Interspace.ID, "en_us");
    }

    @Override
    protected void addBooks(Consumer<BookBuilder> consumer) {
        this.createBookBuilder("index_interspace", "Index Interspace", "")
                .build(consumer);
    }
}
