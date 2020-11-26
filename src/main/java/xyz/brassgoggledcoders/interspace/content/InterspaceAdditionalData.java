package xyz.brassgoggledcoders.interspace.content;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public class InterspaceAdditionalData {
    private static final String[] VOLUMES = new String[]{
            "Zero", "Transient", "Transient", "Transient", "Transient",
            "Transient", "Shallow", "Average", "Shelf", "Deep", "Abyssal", "Cosmic",
    };

    public static void generateText(RegistrateLangProvider provider) {
        int i = 0;
        for (String volume : VOLUMES) {
            provider.add("text.interspace.volume." + i++, volume);
        }
    }
}
