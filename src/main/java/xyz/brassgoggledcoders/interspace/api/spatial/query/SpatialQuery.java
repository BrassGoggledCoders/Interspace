package xyz.brassgoggledcoders.interspace.api.spatial.query;

import xyz.brassgoggledcoders.interspace.api.spatial.query.field.SpatialField;
import xyz.brassgoggledcoders.interspace.api.spatial.query.filter.SpatialFilter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpatialQuery {
    private final List<SpatialFilter> spatialFilters;
    private final SpatialSorting sorting;
    private final Integer limit;

    public SpatialQuery(@Nonnull List<SpatialFilter> spatialFilters, @Nonnull SpatialSorting sorting,
                        @Nullable Integer limit) {
        this.spatialFilters = spatialFilters;
        this.sorting = sorting;
        this.limit = limit;
    }

    public String asSQL() {
        String filterString = buildFilterString();
        String sortString = buildSortString();
        String limitString = buildLimitString();
        return filterString + " " + sortString + " " + limitString;
    }

    private String buildLimitString() {
        return this.getLimit() != null ? "LIMIT " + this.getLimit() : "";
    }

    private String buildSortString() {
        String columnNames = Arrays.stream(this.getSorting().getFields())
                .map(SpatialField::getFieldName)
                .collect(Collectors.joining(" , "));
        String direction = this.getSorting().isAscending() ? "ASC" : "DESC";
        return "ORDER BY " + columnNames + " " + direction;
    }

    private String buildFilterString() {
        return spatialFilters.stream()
                .map(SpatialFilter::toQuery)
                .collect(Collectors.joining(" AND "));
    }

    public List<SpatialFilter> getSpatialFilters() {
        return spatialFilters;
    }

    public SpatialSorting getSorting() {
        return sorting;
    }

    @Nullable
    public Integer getLimit() {
        return limit;
    }
}
