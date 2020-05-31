package xyz.brassgoggledcoders.interspace.api.spacial.type;

import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.interspace.api.functional.TriFunction;

public class SpacialType extends ForgeRegistryEntry<SpacialType> {
    private final TriFunction<SpacialType, IWorld, IChunk, SpacialInstance> creator;
    private String translationKey;
    private ITextComponent displayName;

    public SpacialType(TriFunction<SpacialType, IWorld, IChunk, SpacialInstance> creator) {
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

    public SpacialInstance createInstance(IWorld world, IChunk chunk) {
        return this.creator.apply(this, world, chunk);
    }
}
