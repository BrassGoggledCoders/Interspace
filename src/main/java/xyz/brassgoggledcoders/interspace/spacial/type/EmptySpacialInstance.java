package xyz.brassgoggledcoders.interspace.spacial.type;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.SpacialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spacial.query.Transaction;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialType;

import java.util.Collection;
import java.util.Collections;

public class EmptySpacialInstance extends SpacialInstance {
    public EmptySpacialInstance(SpacialType spacialType, IWorld world, ChunkPos chunkPos) {
        super(spacialType, world, chunkPos);
    }

    @Override
    public Transaction<Collection<SpacialItem>> offer(Collection<SpacialItem> offered) {
        return Transaction.of(offered);
    }

    @Override
    public Transaction<Collection<SpacialItem>> query(SpacialQueryBuilder spacialQueryBuilder) {
        return Transaction.of(Collections.emptyList());
    }

    @Override
    public Transaction<Collection<SpacialItem>> retrieve(SpacialQueryBuilder spacialQueryBuilder) {
        return Transaction.of(Collections.emptyList());
    }
}
