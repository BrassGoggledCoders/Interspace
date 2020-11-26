package xyz.brassgoggledcoders.interspace.api.functional;

public interface ThrowingConsumer<T, E extends Exception> {
    void accept(T t) throws E;
}
