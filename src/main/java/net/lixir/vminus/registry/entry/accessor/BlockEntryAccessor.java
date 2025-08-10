package net.lixir.vminus.registry.entry.accessor;

import net.lixir.vminus.registry.entry.BlockEntry;

import javax.annotation.Nullable;

public interface BlockEntryAccessor extends EntryAccessor<BlockEntry> {
    @Override
    void vminus$setEntry(BlockEntry blockEntry);

    @Nullable
    @Override
    BlockEntry vminus$getEntry();
}