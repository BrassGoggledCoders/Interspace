package xyz.brassgoggledcoders.interspace.api.spacial.query;

import com.google.common.collect.Lists;
import xyz.brassgoggledcoders.interspace.api.spacial.query.field.SpacialField;
import xyz.brassgoggledcoders.interspace.api.spacial.query.filter.SpacialFilter;

import java.util.List;

public class SpacialQueryBuilder {
    private final List<SpacialFilter> spacialFilters;
    private SpacialSorting sorting;
    private int limit;

    private SpacialQueryBuilder() {
        this.spacialFilters = Lists.newArrayList();
        this.sorting = SpacialSorting.of(SpacialField.ID);
    }

    public SpacialQueryBuilder withFilter(SpacialFilter spacialFilter) {
        this.spacialFilters.add(spacialFilter);
        return this;
    }

    public SpacialQueryBuilder withSorting(SpacialSorting sorting) {
        this.sorting = sorting;
        return this;
    }

    public SpacialQueryBuilder withLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public SpacialQuery build() {
        return new SpacialQuery(spacialFilters, sorting, limit);
    }

    public static SpacialQueryBuilder create() {
        return new SpacialQueryBuilder();
    }
}
