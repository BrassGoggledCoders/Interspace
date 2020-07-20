package xyz.brassgoggledcoders.interspace.datagen.model.builderplus;

import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder.ElementBuilder;

import java.util.function.Consumer;

public class ElementBuilderPlus<T extends ModelBuilder<T>> {
    private final ElementBuilder elementBuilder;

    public ElementBuilderPlus(ElementBuilder elementBuilder) {
        this.elementBuilder = elementBuilder;
    }

    public ElementBuilderPlus<T> withFace(Direction direction, Consumer<ElementBuilder.FaceBuilder> face) {
        face.accept(elementBuilder.face(direction));
        return this;
    }

    public ElementBuilderPlus<T> from(float x, float y, float z) {
        elementBuilder.from(x, y, z);
        return this;
    }

    public ElementBuilderPlus<T> to(float x, float y, float z) {
        elementBuilder.to(x, y, z);
        return this;
    }
}
