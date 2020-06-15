package xyz.brassgoggledcoders.interspace.api.spatial.query;

import xyz.brassgoggledcoders.interspace.api.spatial.query.field.SpatialField;

public class SpatialSorting {
    private final SpatialField[] fields;
    private final boolean ascending;

    private SpatialSorting(SpatialField[] fields, boolean ascending) {
        this.fields = fields;
        this.ascending = ascending;
    }

    public boolean isAscending() {
        return ascending;
    }

    public SpatialField[] getFields() {
        return fields;
    }

    public static SpatialSorting of(boolean ascending, SpatialField... fields) {
        fields = fields.length > 0 ? fields : new SpatialField[]{SpatialField.ID};
        return new SpatialSorting(fields, ascending);
    }

    public static SpatialSorting of(SpatialField... fields) {
        return of(true, fields);
    }
}
