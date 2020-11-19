package xyz.brassgoggledcoders.interspace.api.functional;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T value) throws E;
}
