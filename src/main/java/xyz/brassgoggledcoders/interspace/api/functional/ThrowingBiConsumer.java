package xyz.brassgoggledcoders.interspace.api.functional;

public interface ThrowingBiConsumer<T, U, E extends Exception> {
    void accept(T t, U u) throws E;
}
