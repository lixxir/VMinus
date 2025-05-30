package net.lixir.vminus.registry.entry;

import javax.annotation.Nullable;

public interface BlockEntryAccessor extends EntryAccessor<BlockEntry> {
    @Override
    void vminus$setEntry(BlockEntry blockEntry);

    @Nullable
    @Override
    BlockEntry vminus$getEntry();
}
