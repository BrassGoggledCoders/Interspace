package xyz.brassgoggledcoders.interspace.datagen.spacial;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.dimension.DimensionType;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpatialTypes;
import xyz.brassgoggledcoders.interspace.nbt.CompoundNBTBuilder;

public class InterspaceSpatialProvider extends SpatialProvider {
    public InterspaceSpatialProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void registerSpacialWorldEntries() {
        this.getBuilder(DimensionType.OVERWORLD)
                .withEntry(builder -> builder.withType(InterspaceSpatialTypes.BASIC_CACHE)
                        .withNBT(CompoundNBTBuilder.create()
                                .withString("lootTable", "minecraft:chests/abandoned_mineshaft")
                        )
                );

        this.getBuilder(Interspace.rl("default"))
                .withEntry(builder -> builder.withType(InterspaceSpatialTypes.BASIC_CACHE)
                        .withNBT(CompoundNBTBuilder.create()
                                .withString("lootTable", "minecraft:chests/abandoned_mineshaft")
                        )
                );
    }
}
