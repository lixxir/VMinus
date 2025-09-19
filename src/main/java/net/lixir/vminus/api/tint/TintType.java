package net.lixir.vminus.api.tint;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a tint that can be applied to items or blocks.
 * <P>
 * This interface is typically implemented by enums to define a set of available tint types.
 * Each tint type provides a unique ID and optional functions for applying tints to blocks and items.
 * </P>
 */
public interface TintType {
    @NotNull String getName();
    @Nullable ItemTintFunction getItemTint();
    @Nullable BlockTintFunction getBlockTint();

    /**
     * Checks if this tint is effectively empty.
     * @return false if it matches {@link BuiltInTintTypes#NONE} or if it is {@link #isUnset()}
     */
    default boolean isEmpty() {
        return getName().equals(BuiltInTintTypes.NONE.getName()) || isUnset();
    }

    /**
     * Checks if this tint is explicitly unset.
     * @return false if it matches {@link BuiltInTintTypes#UNSET}
     */
    default boolean isUnset() {
        return getName().equals(BuiltInTintTypes.UNSET.getName());
    }
}
