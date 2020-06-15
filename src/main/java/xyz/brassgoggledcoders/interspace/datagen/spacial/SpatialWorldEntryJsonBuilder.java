package xyz.brassgoggledcoders.interspace.datagen.spacial;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.function.Function;

public class SpatialWorldEntryJsonBuilder {
    private boolean replace;
    private final List<SpatialEntryJsonBuilder> entries;

    private SpatialWorldEntryJsonBuilder() {
        this.replace = false;
        this.entries = Lists.newArrayList();
    }

    public SpatialWorldEntryJsonBuilder withReplace() {
        return this.withReplace(true);
    }

    public SpatialWorldEntryJsonBuilder withReplace(boolean replace) {
        this.replace = replace;
        return this;
    }

    public SpatialWorldEntryJsonBuilder withEntry(Function<SpatialEntryJsonBuilder, SpatialEntryJsonBuilder> entryBuilder) {
        return this.withEntry(entryBuilder.apply(SpatialEntryJsonBuilder.create()));
    }

    public SpatialWorldEntryJsonBuilder withEntry(SpatialEntryJsonBuilder entryBuilder) {
        this.entries.add(entryBuilder);
        return this;
    }

    public JsonObject build() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("replace", replace);
        JsonArray jsonEntries = new JsonArray();
        entries.stream()
                .map(SpatialEntryJsonBuilder::build)
                .forEach(jsonEntries::add);
        jsonObject.add("entries", jsonEntries);
        return jsonObject;
    }

    public static SpatialWorldEntryJsonBuilder create() {
        return new SpatialWorldEntryJsonBuilder();
    }
}
