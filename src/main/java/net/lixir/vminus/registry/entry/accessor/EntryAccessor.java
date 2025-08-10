package net.lixir.vminus.registry.entry.accessor;

import javax.annotation.Nullable;

public interface EntryAccessor<T> {
    void vminus$setEntry(T blockEntry);

    @Nullable
    T vminus$getEntry();
}
