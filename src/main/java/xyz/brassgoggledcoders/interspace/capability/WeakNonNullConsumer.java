package xyz.brassgoggledcoders.interspace.capability;

import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraftforge.common.util.NonNullConsumer;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;

public class WeakNonNullConsumer<REF,C> implements NonNullConsumer<C> {
    private final WeakReference<REF> reference;
    private final NonNullBiConsumer<REF,C> consumer;

    /**
     * Creates a new weak consumer wrapper
     * @param ref        Weak reference, typically to a TE
     * @param consumer  Consumer using the TE and the consumed value. Should not use a lambda reference to an object that may need to be garbage collected
     */
    public WeakNonNullConsumer(REF ref, NonNullBiConsumer<REF,C> consumer) {
        this.reference = new WeakReference<>(ref);
        this.consumer = consumer;
    }

    @Override
    public void accept(@Nonnull C c) {
        REF ref = this.reference.get();
        if (ref != null) {
            consumer.accept(ref, c);
        }
    }
}
