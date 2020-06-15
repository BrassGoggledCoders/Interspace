package xyz.brassgoggledcoders.interspace.api.spatial.query;

import com.google.common.collect.Lists;
import xyz.brassgoggledcoders.interspace.api.spatial.query.field.SpatialField;
import xyz.brassgoggledcoders.interspace.api.spatial.query.filter.SpatialFilter;

import java.util.List;

public class SpatialQueryBuilder {
    private final List<SpatialFilter> spatialFilters;
    private SpatialSorting sorting;
    private int limit;

    private SpatialQueryBuilder() {
        this.spatialFilters = Lists.newArrayList();
        this.sorting = SpatialSorting.of(SpatialField.ID);
    }

    public SpatialQueryBuilder withFilter(SpatialFilter spatialFilter) {
        this.spatialFilters.add(spatialFilter);
        return this;
    }

    public SpatialQueryBuilder withSorting(SpatialSorting sorting) {
        this.sorting = sorting;
        return this;
    }

    public SpatialQueryBuilder withLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public SpatialQuery build() {
        return new SpatialQuery(spatialFilters, sorting, limit);
    }

    public static SpatialQueryBuilder create() {
        return new SpatialQueryBuilder();
    }
}
