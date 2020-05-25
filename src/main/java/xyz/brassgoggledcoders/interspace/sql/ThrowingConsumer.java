package xyz.brassgoggledcoders.interspace.sql;

public interface ThrowingConsumer<T, E extends Exception> {
    void accept(T t) throws E;
}
