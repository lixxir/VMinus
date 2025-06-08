package net.lixir.vminus.registry.entry;

import javax.annotation.Nullable;

public interface ItemEntryAccessor extends EntryAccessor<ItemEntry> {
    @Override
    void vminus$setEntry(ItemEntry itemEntry);

    @Nullable
    @Override
    ItemEntry vminus$getEntry();
}