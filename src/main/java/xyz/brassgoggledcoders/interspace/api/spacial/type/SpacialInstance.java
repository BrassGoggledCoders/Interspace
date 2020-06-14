package xyz.brassgoggledcoders.interspace.api.spacial.type;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spacial.capability.IInterspace;
import xyz.brassgoggledcoders.interspace.api.spacial.capability.IInterspaceWorld;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.SpacialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spacial.query.Transaction;
import xyz.brassgoggledcoders.interspace.api.spacial.query.field.SpacialComparison;
import xyz.brassgoggledcoders.interspace.api.spacial.query.field.SpacialField;
import xyz.brassgoggledcoders.interspace.api.spacial.query.filter.SpacialFilter;
import xyz.brassgoggledcoders.interspace.api.spacial.query.filter.ValueSpacialFilter;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class SpacialInstance implements IInterspace, INBTSerializable<CompoundNBT> {
    private final SpacialType spacialType;
    private final IWorld world;
    private final ChunkPos chunkPos;
    private final LazyOptional<IInterspaceWorld> interspaceWorld;
    private final SpacialFilter chunkXFilter;
    private final SpacialFilter chunkZFilter;

    public SpacialInstance(SpacialType spacialType, IWorld world, ChunkPos chunkPos) {
        this.spacialType = spacialType;
        this.world = world;
        this.chunkPos = chunkPos;
        if (world instanceof ICapabilityProvider) {
            this.interspaceWorld = ((ICapabilityProvider) world).getCapability(InterspaceAPI.INTERSPACE_WORLD);
        } else {
            this.interspaceWorld = LazyOptional.empty();
        }
        this.chunkXFilter = new ValueSpacialFilter(SpacialField.CHUNK_X, SpacialComparison.EQUAL_TO, chunkPos.x);
        this.chunkZFilter = new ValueSpacialFilter(SpacialField.CHUNK_Z, SpacialComparison.EQUAL_TO, chunkPos.z);
    }

    public void onLoad() {

    }

    public void tick() {

    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    public IWorld getWorld() {
        return world;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public SpacialType getType() {
        return this.spacialType;
    }

    public ITextComponent getDisplayName() {
        return this.getType().getDisplayName();
    }

    @Override
    public Transaction<Collection<SpacialItem>> offer(Collection<SpacialItem> offered) {
        return interspaceWorld.map(interspace -> interspace.offer(chunkPos, offered))
                .orElseGet(() -> Transaction.of(uuid -> CompletableFuture.completedFuture(offered)));
    }

    @Override
    public Transaction<Collection<SpacialItem>> query(SpacialQueryBuilder spacialQueryBuilder) {
        return interspaceWorld.map(interspace -> interspace.query(spacialQueryBuilder.withFilter(chunkXFilter).withFilter(chunkZFilter)))
                .orElseGet(() -> Transaction.of(uuid -> CompletableFuture.completedFuture(Collections.emptyList())));
    }

    @Override
    public Transaction<Collection<SpacialItem>> retrieve(SpacialQueryBuilder spacialQueryBuilder) {
        return interspaceWorld.map(interspace -> interspace.query(spacialQueryBuilder.withFilter(chunkXFilter).withFilter(chunkZFilter)))
                .orElseGet(() -> Transaction.of(uuid -> CompletableFuture.completedFuture(Collections.emptyList())));

    }
}
