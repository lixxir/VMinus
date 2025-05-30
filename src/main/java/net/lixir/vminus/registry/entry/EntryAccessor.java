package net.lixir.vminus.registry.entry;

import javax.annotation.Nullable;

public interface EntryAccessor<T> {
    void vminus$setEntry(T blockEntry);

    @Nullable
    T vminus$getEntry();
}
