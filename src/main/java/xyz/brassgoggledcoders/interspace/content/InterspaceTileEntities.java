package xyz.brassgoggledcoders.interspace.content;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.tileentity.ObeliskConnectedTileEntity;
import xyz.brassgoggledcoders.interspace.tileentity.ObeliskControllerTileEntity;
import xyz.brassgoggledcoders.interspace.tileentity.QuerySlateTileEntity;

public class InterspaceTileEntities {
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(
            ForgeRegistries.TILE_ENTITIES, Interspace.ID);

    public static final RegistryObject<TileEntityType<ObeliskControllerTileEntity>> OBELISK_CONTROLLER =
            TILE_ENTITIES.register("obelisk_controller", () -> TileEntityType.Builder
                    .create(ObeliskControllerTileEntity::new, InterspaceBlocks.OBELISK_CORE.get())
                    .build(null));

    public static final RegistryObject<TileEntityType<ObeliskConnectedTileEntity>> OBELISK_CONNECTED =
            TILE_ENTITIES.register("obelisk_connected", () -> TileEntityType.Builder
                    .create(ObeliskConnectedTileEntity::new, InterspaceBlocks.OBELISK_CORE.get())
                    .build(null));

    public static final RegistryObject<TileEntityType<QuerySlateTileEntity>> QUERY_SLATE =
            TILE_ENTITIES.register("query_slate", () -> TileEntityType.Builder
            .create(QuerySlateTileEntity::new, InterspaceBlocks.QUERY_SLATE.get())
            .build(null));

    public static void register(IEventBus modBus) {
        TILE_ENTITIES.register(modBus);
    }
}
