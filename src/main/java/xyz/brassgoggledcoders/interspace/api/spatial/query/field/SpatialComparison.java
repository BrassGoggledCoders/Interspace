package xyz.brassgoggledcoders.interspace.api.spatial.query.field;

import com.google.common.collect.Sets;
import joptsimple.internal.Strings;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;

public enum SpatialComparison {
    GREATER_THAN("%s > ?", SpatialFieldType.LONG),
    GREATER_OR_EQUAL("%s >= ?", SpatialFieldType.LONG),
    EQUAL_TO("%s = ?", SpatialFieldType.LONG, SpatialFieldType.STRING),
    LESS_OR_EQUAL("%s =< ?", SpatialFieldType.LONG),
    LESS_THAN("%s < ?", SpatialFieldType.LONG),
    LIKE("%s like ?", SpatialFieldType.STRING),
    IN(SpatialComparison::createIn, SpatialFieldType.LONG, SpatialFieldType.STRING);



    private final Function<Integer, String> formatStringCreator;
    private final Set<SpatialFieldType> allowedTypes;

    SpatialComparison(Function<Integer, String> formatStringCreator, SpatialFieldType... allowedTypes) {
        this.formatStringCreator = formatStringCreator;
        this.allowedTypes = Sets.newHashSet(allowedTypes);
    }

    SpatialComparison(String formatString, SpatialFieldType... allowedTypes) {
        this.formatStringCreator = amount -> formatString;
        this.allowedTypes = Sets.newHashSet(allowedTypes);
    }

    @Nullable
    public String parse(SpatialField spatialField) {
        return parse(spatialField, 1);
    }

    @Nullable
    public String parse(SpatialField spatialField, int numberOfValues) {
        if (allowedTypes.contains(spatialField.getType())) {
            return String.format(formatStringCreator.apply(numberOfValues), spatialField.getFieldName());
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
