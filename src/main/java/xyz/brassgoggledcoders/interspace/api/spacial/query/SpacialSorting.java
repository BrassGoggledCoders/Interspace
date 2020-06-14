package xyz.brassgoggledcoders.interspace.api.spacial.query;

import xyz.brassgoggledcoders.interspace.api.spacial.query.field.SpacialField;

public class SpacialSorting {
    private final SpacialField[] fields;
    private final boolean ascending;

    private SpacialSorting(SpacialField[] fields, boolean ascending) {
        this.fields = fields;
        this.ascending = ascending;
    }

    public boolean isAscending() {
        return ascending;
    }

    public SpacialField[] getFields() {
        return fields;
    }

    public static SpacialSorting of(boolean ascending, SpacialField... fields) {
        fields = fields.length > 0 ? fields : new SpacialField[]{SpacialField.ID};
        return new SpacialSorting(fields, ascending);
    }

    public static SpacialSorting of(SpacialField... fields) {
        return of(true, fields);
    }
}
