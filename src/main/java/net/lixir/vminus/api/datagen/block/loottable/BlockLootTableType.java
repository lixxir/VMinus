package net.lixir.vminus.api.datagen.block.loottable;


import net.lixir.vminus.api.datagen.block.BlockData;
import net.lixir.vminus.api.registry.definition.BlockDefinition;
import net.lixir.vminus.api.datagen.block.loottable.provider.VBlockLootProvider;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

/**
 * Defines a type of block loot table behavior.
 * <p>
 * Implementations (usually enums like {@link BuiltInBlockLootTableTypes})
 * determine how loot is generated for a block during data generation.
 * Each type wraps a {@link BiConsumer} that applies loot rules for a block
 * and its {@link BlockDefinition}.
 */
public interface BlockLootTableType {

    /**
     * Applies this loot table behavior to the block from the given block data using the provided loot table provider.
     *
     * @param blockData    the block data to process
     * @param provider the provider used to apply loot rules
     */
    void apply(BlockData blockData, VBlockLootProvider provider);

    /**
     * Returns the consumer that applies this loot table behavior.
     *
     * @return the loot table consumer
     */
    BiConsumer<BlockData, VBlockLootProvider> getConsumer();

    /**
     * Returns the internal identifier of this loot table type.
     *
     * @return the loot table type key
     */
    String getName();

    /**
     * Checks if this loot type is empty (none or unset).
     *
     * @return true if empty
     */
    default boolean isEmpty() {
        return this.getName().equals(BuiltInBlockLootTableTypes.NONE.getName()) || isUnset();
    }

    /**
     * Checks if this loot type is the unset placeholder.
     *
     * @return true if unset
     */
    default boolean isUnset() {
        return this == BuiltInBlockLootTableTypes.UNSET;
    }
}

