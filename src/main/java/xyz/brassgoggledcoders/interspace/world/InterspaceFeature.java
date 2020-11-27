package xyz.brassgoggledcoders.interspace.world;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import xyz.brassgoggledcoders.interspace.task.interspace.SetupChunkInterspaceTask;
import xyz.brassgoggledcoders.interspace.task.interspace.SetupWorldInterspaceTask;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class InterspaceFeature extends Feature<NoFeatureConfig> {
    public InterspaceFeature() {
        super(NoFeatureConfig.field_236558_a_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        RegistryKey<World> registryKey = reader.getWorld().getDimensionKey();
        SetupWorldInterspaceTask.submit(registryKey);
        SetupChunkInterspaceTask.submit(registryKey, rand, new ChunkPos(pos));
        return true;
    }
}
