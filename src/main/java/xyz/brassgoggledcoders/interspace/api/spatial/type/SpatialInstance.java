package xyz.brassgoggledcoders.interspace.api.spatial.type;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialWorld;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.query.SpatialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;
import xyz.brassgoggledcoders.interspace.api.spatial.query.field.SpatialComparison;
import xyz.brassgoggledcoders.interspace.api.spatial.query.field.SpatialField;
import xyz.brassgoggledcoders.interspace.api.spatial.query.filter.SpatialFilter;
import xyz.brassgoggledcoders.interspace.api.spatial.query.filter.ValueSpatialFilter;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class SpatialInstance implements ISpatial, INBTSerializable<CompoundNBT> {
    private final SpatialType spatialType;
    private final IWorld world;
    private final ChunkPos chunkPos;
    private final LazyOptional<ISpatialWorld> interspaceWorld;
    private final SpatialFilter chunkXFilter;
    private final SpatialFilter chunkZFilter;

    private ITextComponent customName = null;

    public SpatialInstance(SpatialType spatialType, IWorld world, ChunkPos chunkPos) {
        this.spatialType = spatialType;
        this.world = world;
        this.chunkPos = chunkPos;
        if (world instanceof ICapabilityProvider) {
            this.interspaceWorld = ((ICapabilityProvider) world).getCapability(InterspaceAPI.SPATIAL_WORLD);
        } else {
            this.interspaceWorld = LazyOptional.empty();
        }
        this.chunkXFilter = new ValueSpatialFilter(SpatialField.CHUNK_X, SpatialComparison.EQUAL_TO, chunkPos.x);
        this.chunkZFilter = new ValueSpatialFilter(SpatialField.CHUNK_Z, SpatialComparison.EQUAL_TO, chunkPos.z);
    }

    public void onLoad() {

    }

    public void tick() {

    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        if (customName != null) {
            compoundNBT.putString("customName", ITextComponent.Serializer.toJson(customName));
        }
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT compoundNBT) {
        if (compoundNBT.contains("customName", Constants.NBT.TAG_STRING)) {
            this.customName = ITextComponent.Serializer.getComponentFromJson(compoundNBT.getString("customName"));
        } else if (compoundNBT.contains("customName", Constants.NBT.TAG_COMPOUND)) {
            this.customName = ITextComponent.Serializer.getComponentFromJson(compoundNBT.getCompound("customName").toString());
        }
    }

    public IWorld getWorld() {
        return world;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public SpatialType getType() {
        return this.spatialType;
    }

    public ITextComponent getDisplayName() {
        return this.customName != null ? this.customName : this.getType().getDisplayName();
    }

    @Override
    public Transaction<Collection<SpatialItem>> offer(Collection<SpatialItem> offered) {
        return interspaceWorld.map(interspace -> interspace.offer(chunkPos, offered))
                .orElseGet(() -> Transaction.of(uuid -> CompletableFuture.completedFuture(offered)));
    }

    @Override
    public Transaction<Collection<SpatialItem>> query(SpatialQueryBuilder spatialQueryBuilder) {
        return interspaceWorld.map(interspace -> interspace.query(spatialQueryBuilder.withFilter(chunkXFilter).withFilter(chunkZFilter)))
                .orElseGet(() -> Transaction.of(uuid -> CompletableFuture.completedFuture(Collections.emptyList())));
    }

    @Override
    public Transaction<Collection<SpatialItem>> retrieve(SpatialQueryBuilder spatialQueryBuilder) {
        return interspaceWorld.map(interspace -> interspace.query(spatialQueryBuilder.withFilter(chunkXFilter).withFilter(chunkZFilter)))
                .orElseGet(() -> Transaction.of(uuid -> CompletableFuture.completedFuture(Collections.emptyList())));

    }
}
