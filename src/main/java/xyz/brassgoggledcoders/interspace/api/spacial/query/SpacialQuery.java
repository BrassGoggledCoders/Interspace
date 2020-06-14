package xyz.brassgoggledcoders.interspace.api.spacial.query;

import xyz.brassgoggledcoders.interspace.api.spacial.query.field.SpacialField;
import xyz.brassgoggledcoders.interspace.api.spacial.query.filter.SpacialFilter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpacialQuery {
    private final List<SpacialFilter> spacialFilters;
    private final SpacialSorting sorting;
    private final Integer limit;

    public SpacialQuery(@Nonnull List<SpacialFilter> spacialFilters, @Nonnull SpacialSorting sorting,
                        @Nullable Integer limit) {
        this.spacialFilters = spacialFilters;
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
                .map(SpacialField::getFieldName)
                .collect(Collectors.joining(" , "));
        String direction = this.getSorting().isAscending() ? "ASC" : "DESC";
        return "ORDER BY " + columnNames + " " + direction;
    }

    private String buildFilterString() {
        return spacialFilters.stream()
                .map(SpacialFilter::toQuery)
                .collect(Collectors.joining(" AND "));
    }

    public List<SpacialFilter> getSpacialFilters() {
        return spacialFilters;
    }

    public SpacialSorting getSorting() {
        return sorting;
    }

    @Nullable
    public Integer getLimit() {
        return limit;
    }
}
