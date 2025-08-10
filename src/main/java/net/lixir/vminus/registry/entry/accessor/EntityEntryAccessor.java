package net.lixir.vminus.registry.entry.accessor;

import net.lixir.vminus.registry.entry.EntityEntry;

import javax.annotation.Nullable;

public interface EntityEntryAccessor extends EntryAccessor<EntityEntry> {
    @Override
    void vminus$setEntry(EntityEntry entry);

    @Nullable
    @Override
    EntityEntry vminus$getEntry();
}