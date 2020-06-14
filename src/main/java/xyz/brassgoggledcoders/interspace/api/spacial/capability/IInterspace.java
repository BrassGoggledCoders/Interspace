package xyz.brassgoggledcoders.interspace.api.spacial.capability;

import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.SpacialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spacial.query.Transaction;

import java.util.Collection;

public interface IInterspace {
    Transaction<Collection<SpacialItem>> offer(Collection<SpacialItem> offered);

    Transaction<Collection<SpacialItem>> query(SpacialQueryBuilder spacialQueryBuilder);

    Transaction<Collection<SpacialItem>> retrieve(SpacialQueryBuilder spacialQueryBuilder);
}
