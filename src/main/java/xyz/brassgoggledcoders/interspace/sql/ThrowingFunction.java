package xyz.brassgoggledcoders.interspace.sql;

public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T value) throws E;
}
