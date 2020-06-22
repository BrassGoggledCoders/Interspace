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
    private final int limit;
    private final int maxSize;

    public SpatialQuery(@Nonnull List<SpatialFilter> spatialFilters, @Nonnull SpatialSorting sorting,
                        int limit, int maxSize) {
        this.spatialFilters = spatialFilters;
        this.sorting = sorting;
        this.limit = limit;
        this.maxSize = maxSize;
    }

    public String asSQL() {
        String filterString = buildFilterString();
        String sortString = buildSortString();
        String limitString = buildLimitString();
        return filterString + " " + sortString + " " + limitString;
    }

    public int getMaxSize() {
        return maxSize;
    }

    private String buildLimitString() {
        return this.getLimit() > 0 ? "LIMIT " + this.getLimit() : "";
    }

    private String buildSortString() {
        String columnNames = Arrays.stream(this.getSorting().getFields())
                .map(SpatialField::getFieldName)
                .collect(Collectors.joining(" , "));
        String direction = this.getSorting().isAscending() ? "ASC" : "DESC";
        return "ORDER BY " + columnNames + " " + direction;
    }

    private String buildFilterString() {
        return this.getSpatialFilters().isEmpty() ? "" : "WHERE " + this.getSpatialFilters()
                .stream()
                .map(SpatialFilter::toQuery)
                .collect(Collectors.joining(" AND "));
    }

    public List<SpatialFilter> getSpatialFilters() {
        return spatialFilters;
    }

    public SpatialSorting getSorting() {
        return sorting;
    }

    public int getLimit() {
        return limit;
    }
}
