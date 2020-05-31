package xyz.brassgoggledcoders.interspace.api.functional;

@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T tValue, U uValue, V vValue);
}
