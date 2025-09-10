package net.lixir.vminus.api.datagen.block.model;

import net.lixir.vminus.api.datagen.block.BlockData;
import net.lixir.vminus.api.datagen.block.model.provider.VBlockStateProvider;
import net.lixir.vminus.api.datagen.item.model.ItemModelType;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

/**
 * Represents a type of block model definition.
 * Implementations (usually enums) define how models are generated
 * for blocks and their associated items during data generation.
 */
public interface BlockModelType {
    /**
     * Applies this model definition to a block during datagen.
     *
     * @param block           the target block
     * @param provider        the blockstate/model provider
     */
    void apply(Block block, VBlockStateProvider provider);

    /**
     * Returns the consumer function that applies this model definition.
     */
    BiConsumer<BlockData, VBlockStateProvider> getConsumer();

    /**
     * @return the item model type linked to this block model type
     */
    ItemModelType getItemModelType();

    /**
     * @return the internal key of this block model type
     */
    String getName();

    /**
     * @return true if this model type is {@link BuiltInBlockModelTypes#NONE} or {@link #isUnset()} returns true
     */
    default boolean isEmpty() {
        return this.getName().equals(BuiltInBlockModelTypes.NONE.getName()) || isUnset();
    }

    /**
     * @return true if this model type is {@link BuiltInBlockModelTypes#UNSET}
     */
    default boolean isUnset() {
        return this.getName().equals(BuiltInBlockModelTypes.UNSET.getName());
    }
}
