package xyz.brassgoggledcoders.interspace.content;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.item.MirrorItem;

public class InterspaceItems {
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Interspace.ID);

    public static final RegistryObject<MirrorItem> MIRROR = ITEMS.register("mirror", MirrorItem::new);

    public static final RegistryObject<Item> NAFASI_INGOT = ITEMS.register("nafasi_ingot", InterspaceItems::genericItem);
    public static final RegistryObject<Item> NAFASI_NUGGET = ITEMS.register("nafasi_nugget", InterspaceItems::genericItem);

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    public static Item genericItem() {
        return new Item(new Item.Properties()
                .group(Interspace.ITEM_GROUP)
        );
    }
}
