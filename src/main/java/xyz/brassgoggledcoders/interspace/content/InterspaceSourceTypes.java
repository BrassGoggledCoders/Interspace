package xyz.brassgoggledcoders.interspace.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.source.SourceType;
import xyz.brassgoggledcoders.interspace.api.source.WorldSource;

public class InterspaceSourceTypes {

    public static final RegistryEntry<SourceType> WORLD = InterspaceMod.getRegistrate()
            .object("world")
            .simple(SourceType.class, () -> SourceType.of(WorldSource::new));

    public static void setup() {

    }
}
