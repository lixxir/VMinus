package net.lixir.vminus.registry.entry.accessor;

import net.lixir.vminus.registry.entry.ItemEntry;

import javax.annotation.Nullable;

public interface ItemEntryAccessor extends EntryAccessor<ItemEntry> {
    @Override
    void vminus$setEntry(ItemEntry itemEntry);

    @Nullable
    @Override
    ItemEntry vminus$getEntry();
}