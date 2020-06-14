package xyz.brassgoggledcoders.interspace.api.spacial.query.field;

import com.google.common.collect.Sets;
import joptsimple.internal.Strings;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

public enum SpacialComparison {
    GREATER_THAN("%s > ?", SpacialFieldType.LONG),
    GREATER_OR_EQUAL("%s >= ?", SpacialFieldType.LONG),
    EQUAL_TO("%s = ?", SpacialFieldType.LONG, SpacialFieldType.STRING),
    LESS_OR_EQUAL("%s =< ?", SpacialFieldType.LONG),
    LESS_THAN("%s < ?", SpacialFieldType.LONG),
    LIKE("%s like ?", SpacialFieldType.STRING),
    IN(SpacialComparison::createIn, SpacialFieldType.LONG, SpacialFieldType.STRING);



    private final Function<Integer, String> formatStringCreator;
    private final Set<SpacialFieldType> allowedTypes;

    SpacialComparison(Function<Integer, String> formatStringCreator, SpacialFieldType... allowedTypes) {
        this.formatStringCreator = formatStringCreator;
        this.allowedTypes = Sets.newHashSet(allowedTypes);
    }

    SpacialComparison(String formatString, SpacialFieldType... allowedTypes) {
        this.formatStringCreator = amount -> formatString;
        this.allowedTypes = Sets.newHashSet(allowedTypes);
    }

    @Nullable
    public String parse(SpacialField spacialField) {
        return parse(spacialField, 1);
    }

    @Nullable
    public String parse(SpacialField spacialField, int numberOfValues) {
        if (allowedTypes.contains(spacialField.getType())) {
            return String.format(formatStringCreator.apply(numberOfValues), spacialField.getFieldName());
        } else {
            return null;
        }
    }

    private static String createIn(Integer size) {
        String[] strings = new String[size];
        Arrays.fill(strings, "?");
        return "%s IN (" + Strings.join(strings, ",") + ")";
    }
}
