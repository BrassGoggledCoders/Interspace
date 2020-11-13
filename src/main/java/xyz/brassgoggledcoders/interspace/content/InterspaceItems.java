package xyz.brassgoggledcoders.interspace.content;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.item.Item;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.item.MirrorItem;

public class InterspaceItems {

    public static final ItemEntry<MirrorItem> MIRROR = Interspace.getRegistrate()
            .object("mirror")
            .item(MirrorItem::new)
            .model((context, provider) -> provider.handheld(context))
            .register();

    public static final ItemEntry<Item> NAFASI_INGOT = Interspace.getRegistrate()
            .object("nafasi_ingot")
            .item(Item::new)
            .register();

    public static final ItemEntry<Item> NAFASI_NUGGET = Interspace.getRegistrate()
            .object("nafasi_nugget")
            .item(Item::new)
            .register();

    public static void setup() {
    }

}
