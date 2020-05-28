package xyz.brassgoggledcoders.interspace.content;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.entity.QuerySlateEntity;

public class InterspaceEntities {
    private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES,
            Interspace.ID);

    public static final RegistryObject<EntityType<QuerySlateEntity>> QUERY_SLATE = ENTITIES.register("query_slate",
            () -> EntityType.Builder.<QuerySlateEntity>create(QuerySlateEntity::new, EntityClassification.MISC)
                    .size(0.5F, 0.5F)
                    .setUpdateInterval(Integer.MAX_VALUE)
                    .build(Interspace.ID + ":query_slate"));

    public static void register(IEventBus modBus) {
        ENTITIES.register(modBus);
    }
}
