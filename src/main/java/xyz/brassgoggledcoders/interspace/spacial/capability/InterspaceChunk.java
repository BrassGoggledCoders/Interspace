package xyz.brassgoggledcoders.interspace.spacial.capability;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullLazy;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spacial.capability.IInterspaceChunk;
import xyz.brassgoggledcoders.interspace.api.spacial.capability.IInterspaceWorld;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.SpacialQuery;
import xyz.brassgoggledcoders.interspace.api.spacial.query.SpacialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spacial.query.Transaction;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpacialTypes;

import javax.annotation.Nonnull;
import java.util.Collection;

public class InterspaceChunk implements IInterspaceChunk {
    private final NonNullLazy<LazyOptional<IInterspaceWorld>> worldProvider;
    private final NonNullLazy<SpacialInstance> spacialProvider;

    public InterspaceChunk(World world, ChunkPos chunkPos) {
        worldProvider = NonNullLazy.of(() -> world.getCapability(InterspaceAPI.INTERSPACE_WORLD));
        spacialProvider = NonNullLazy.of(() -> worldProvider.get()
                .map(interspaceWorld -> interspaceWorld.getSpacialInstance(chunkPos))
                .orElseGet(() -> InterspaceSpacialTypes.EMPTY.get().createInstance(world, chunkPos)));
    }

    @Override
    @Nonnull
    public SpacialInstance getSpacialInstance() {
        return spacialProvider.get();
    }

    @Override
    public Transaction<Collection<SpacialItem>> offer(Collection<SpacialItem> offered) {
        return this.getSpacialInstance().offer(offered);
    }

    @Override
    public Transaction<Collection<SpacialItem>> query(SpacialQueryBuilder spacialQueryBuilder) {
        return this.getSpacialInstance().query(spacialQueryBuilder);
    }

    @Override
    public Transaction<Collection<SpacialItem>> retrieve(SpacialQueryBuilder spacialQueryBuilder) {
        return this.getSpacialInstance().retrieve(spacialQueryBuilder);
    }
}
