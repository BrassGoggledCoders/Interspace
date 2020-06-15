package xyz.brassgoggledcoders.interspace.datagen.spacial;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.function.Function;

public class SpacialWorldEntryJsonBuilder {
    private boolean replace;
    private final List<SpacialEntryJsonBuilder> entries;

    private SpacialWorldEntryJsonBuilder() {
        this.replace = false;
        this.entries = Lists.newArrayList();
    }

    public SpacialWorldEntryJsonBuilder withReplace() {
        return this.withReplace(true);
    }

    public SpacialWorldEntryJsonBuilder withReplace(boolean replace) {
        this.replace = replace;
        return this;
    }

    public SpacialWorldEntryJsonBuilder withEntry(Function<SpacialEntryJsonBuilder, SpacialEntryJsonBuilder> entryBuilder) {
        return this.withEntry(entryBuilder.apply(SpacialEntryJsonBuilder.create()));
    }

    public SpacialWorldEntryJsonBuilder withEntry(SpacialEntryJsonBuilder entryBuilder) {
        this.entries.add(entryBuilder);
        return this;
    }

    public JsonObject build() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("replace", replace);
        JsonArray jsonEntries = new JsonArray();
        entries.stream()
                .map(SpacialEntryJsonBuilder::build)
                .forEach(jsonEntries::add);
        jsonObject.add("entries", jsonEntries);
        return jsonObject;
    }

    public static SpacialWorldEntryJsonBuilder create() {
        return new SpacialWorldEntryJsonBuilder();
    }
}
