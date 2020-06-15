package xyz.brassgoggledcoders.interspace.api.spatial.capability;

import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.query.SpatialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;

import java.util.Collection;

public interface ISpatial {
    Transaction<Collection<SpatialItem>> offer(Collection<SpatialItem> offered);

    Transaction<Collection<SpatialItem>> query(SpatialQueryBuilder spatialQueryBuilder);

    Transaction<Collection<SpatialItem>> retrieve(SpatialQueryBuilder spatialQueryBuilder);
}
