package xyz.brassgoggledcoders.interspace.api.spatial.type;

import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.interspace.api.functional.TriFunction;

public class SpatialType extends ForgeRegistryEntry<SpatialType> {
    private final TriFunction<SpatialType, IWorld, ChunkPos, SpatialInstance> creator;
    private String translationKey;
    private ITextComponent displayName;

    public SpatialType(TriFunction<SpatialType, IWorld, ChunkPos, SpatialInstance> creator) {
        this.creator = creator;
    }

    public ITextComponent getDisplayName() {
        if (this.displayName == null) {
            this.displayName = new TranslationTextComponent(this.getTranslationKey());
        }
        return this.displayName;
    }

    public String getTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.makeTranslationKey("spacial_type", this.getRegistryName());
        }
        return this.translationKey;
    }

    public SpatialInstance createInstance(IWorld world, ChunkPos chunk) {
        return this.creator.apply(this, world, chunk);
    }
}
