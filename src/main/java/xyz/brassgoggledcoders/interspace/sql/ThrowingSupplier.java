package xyz.brassgoggledcoders.interspace.sql;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;
}
