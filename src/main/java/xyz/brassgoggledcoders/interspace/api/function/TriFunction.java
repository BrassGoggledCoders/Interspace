package xyz.brassgoggledcoders.interspace.api.function;

@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T tValue, U uValue, V vValue);
}
