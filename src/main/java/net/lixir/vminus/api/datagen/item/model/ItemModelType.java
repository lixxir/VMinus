package net.lixir.vminus.api.datagen.item.model;

import net.lixir.vminus.api.datagen.item.ItemData;
import net.lixir.vminus.api.datagen.item.model.provider.VItemModelProvider;

/**
 * Represents a type of item model definition.
 * Implementations (usually enums) define how models are generated for items during data generation.
 */
public interface ItemModelType {
    /**
     * Applies this item model definition using the provided model provider.
     *
     * @param itemData    the item data and its definition
     * @param provider the item model provider
     */
    void apply(ItemData itemData, VItemModelProvider provider);

    /** @return the internal name/key of this item model type. */
    String getName();

    /**
     * @return true if this model type is {@link BuiltInItemModelTypes#NONE} or {@link #isUnset()} returns true
     */
    default boolean isEmpty() {
        return this.getName().equals(BuiltInItemModelTypes.NONE.getName()) || isUnset();
    }

    /**
     * @return true if this model type is {@link BuiltInItemModelTypes#UNSET}
     */
    default boolean isUnset() {
        return this.getName().equals(BuiltInItemModelTypes.UNSET.getName());
    }
}
