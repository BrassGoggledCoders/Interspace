package xyz.brassgoggledcoders.interspace.api.spacial.parameter;

import com.google.common.collect.Sets;

import javax.annotation.Nullable;
import java.util.Set;

public enum SpacialComparison {
    GREATER_THAN("%s > ?", SpacialParameterType.LONG),
    GREATER_OR_EQUAL("%s >= ?", SpacialParameterType.LONG),
    EQUAL_TO("%s = ?", SpacialParameterType.LONG, SpacialParameterType.STRING),
    LESS_OR_EQUAL("%s =< ?", SpacialParameterType.LONG),
    LESS_THAN("%s < ?", SpacialParameterType.LONG),
    LIKE("%s like ?", SpacialParameterType.STRING),
    IN("%s in (?)", SpacialParameterType.LONG, SpacialParameterType.STRING);

    private final String formatString;
    private final Set<SpacialParameterType> allowedTypes;

    SpacialComparison(String formatString, SpacialParameterType... allowedTypes) {
        this.formatString = formatString;
        this.allowedTypes = Sets.newHashSet(allowedTypes);
    }

    @Nullable
    public String parse(SpacialField spacialField) {
        if (allowedTypes.contains(spacialField.getType())) {
            return String.format(formatString, spacialField.getFieldName());
        } else {
            return null;
        }
    }
}
