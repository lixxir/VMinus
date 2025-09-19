package net.lixir.vminus.api.tint;

import net.minecraft.world.item.ItemStack;

/**
 * Represents a function that determines the color tint for an {@link ItemStack}.
 * <p>
 * This is a functional interface, so it can be implemented using a lambda
 * or method reference. The returned integer should be a packed RGB color
 * in the format {@code 0xRRGGBB}.
 * <p>
 * Typically used in conjunction with {@link TintType}
 * to provide custom item tints based on the item or tint index.
 */
@FunctionalInterface
public interface ItemTintFunction {
    /**
     * Applies the tint function to the given item stack and tint index.
     *
     * @param stack the {@link ItemStack} to get the tint for
     * @param tintIndex the index of the tint layer (may be null if unspecified)
     * @return the RGB color of the tint as an integer (0xRRGGBB)
     */
    int apply(ItemStack stack, Integer tintIndex);
}
