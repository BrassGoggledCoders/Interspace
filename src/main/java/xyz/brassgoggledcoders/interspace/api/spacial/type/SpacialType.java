package xyz.brassgoggledcoders.interspace.api.spacial.type;

import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Function;

public class SpacialType extends ForgeRegistryEntry<SpacialType> {
    private final Function<SpacialType, SpacialInstance> creator;
    private String translationKey;
    private ITextComponent displayName;

    public SpacialType(Function<SpacialType, SpacialInstance> creator) {
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

    public SpacialInstance createInstance() {
        return this.creator.apply(this);
    }
}
