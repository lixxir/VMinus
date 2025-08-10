package net.lixir.vminus.datagen.util.loottable;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.datagen.BlockLootTable;
import net.lixir.vminus.registry.VRegistry;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.accessor.BlockEntryAccessor;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public abstract class VBlockLootProvider extends BlockLootSubProvider {
    private final String modId;

    public VBlockLootProvider(String modId) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        this.modId = modId;
    }

    public String getModId() {
        return modId;
    }

    @Override
    protected void generate() {
        var blocks = VRegistry.fromId(modId).getBlocks();
        for (Block block : blocks) {
            if (block.getLootTable().equals(BuiltInLootTables.EMPTY))
                continue;
            BlockEntryAccessor accessor = (BlockEntryAccessor) block;
            BlockEntry blockEntry = accessor.vminus$getEntry();
            if (blockEntry == null)
                continue;
            BlockLootTable lootTable = blockEntry.getLootTable();
            VMinus.LOGGER.info("{} has loot table {}", block, lootTable);
            lootTable.apply(block, blockEntry, this);
        }
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return VRegistry.fromId(modId).getBlocks().stream()
                .filter(block -> {
                    if (block.getLootTable().equals(BuiltInLootTables.EMPTY))
                        return false;
                    BlockEntry blockEntry = ((BlockEntryAccessor) block).vminus$getEntry();
                    if (blockEntry == null)
                        return false;
                    BlockLootTable lootTable = blockEntry.getLootTable();
                    return lootTable != BlockLootTable.UNSET && lootTable != BlockLootTable.NONE;
                })
                .toList();
    }

    public void self(Block block) {
        dropSelf(block);
    }

    public <T> Optional<T> as(@NotNull Class<T> clazz) {
        return clazz.isInstance(this) ? Optional.of(clazz.cast(this)) : Optional.empty();
    }
}
