package xyz.brassgoggledcoders.interspace.api.spatial.query;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Transaction<T> {
    private final UUID id;
    private final CompletableFuture<T> result;

    private Transaction(UUID id, CompletableFuture<T> result) {
        this.id = id;
        this.result = result;
    }

    public UUID getId() {
        return id;
    }

    public CompletableFuture<T> getResult() {
        return result;
    }

    public boolean isDone() {
        return this.getResult().isDone();
    }

    public static <U> Transaction<U> of(Function<UUID, CompletableFuture<U>> result) {
        UUID uuid = UUID.randomUUID();
        return new Transaction<>(uuid, result.apply(uuid));
    }

    public static <U> Transaction<U> of(U result) {
        UUID uuid = UUID.randomUUID();
        return new Transaction<>(uuid, CompletableFuture.completedFuture(result));
    }

    public static <U> Transaction<Collection<U>> ofEmpty() {
        return Transaction.of(Collections.emptyList());
    }

    public static <U> Transaction<U> ofEmptyCast() {
        return Transaction.of(null);
    }
}
