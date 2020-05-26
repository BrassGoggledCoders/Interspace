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

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}
