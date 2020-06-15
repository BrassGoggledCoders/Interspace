package xyz.brassgoggledcoders.interspace.datagen.spatial;

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
                )
                .withEntry(build -> build.withType(InterspaceSpatialTypes.SHALLOW_SPRING)
                        .withNBT(CompoundNBTBuilder.create()
                                .withString("lootTable", "interspace:spatial/lava_spring")
                        )
                )
                .withEntry(build -> build.withType(InterspaceSpatialTypes.SHALLOW_SPRING)
                        .withNBT(CompoundNBTBuilder.create()
                                .withString("lootTable", "interspace:spatial/water_spring")
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
