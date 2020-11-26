package xyz.brassgoggledcoders.interspace.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.world.InterspaceFeature;

public class InterspaceFeatures {

    public static final RegistryEntry<InterspaceFeature> INTERSPACE = InterspaceMod.getRegistrate()
            .object("interspace")
            .simple(Feature.class, InterspaceFeature::new);

    public static final NonNullLazy<ConfiguredFeature<?, ?>> CONFIGURED_INTERSPACE =
            NonNullLazy.of(() -> INTERSPACE.get()
                    .withConfiguration(new NoFeatureConfig())
                    .withPlacement(Placement.COUNT.configure(new FeatureSpreadConfig(
                            FeatureSpread.func_242252_a(1)
                    )))
            );

    public static void registerFeatures() {
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, InterspaceMod.rl("interspace"),
                CONFIGURED_INTERSPACE.get());
    }

    public static void onBiomeLoad(BiomeLoadingEvent event) {
        event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CONFIGURED_INTERSPACE.get());
    }

    public static void setup() {

    }
}
