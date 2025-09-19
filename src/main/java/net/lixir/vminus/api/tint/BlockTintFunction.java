package net.lixir.vminus.api.tint;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Represents a function that determines the color tint for a block.
 * <p>
 * This is a functional interface, so it can be implemented using a lambda
 * or method reference. The returned integer should be a packed RGB color
 * in the format {@code 0xRRGGBB}.
 * <p>
 * Typically used in conjunction with {@link TintType}
 * to provide custom block tints based on the block state, world, position,
 * or tint index.
 */
@FunctionalInterface
public interface BlockTintFunction {
    /**
     * Applies the tint function to the given block.
     *
     * @param state the {@link BlockState} of the block
     * @param world a {@link BlockAndTintGetter} representing the world context
     * @param pos   the {@link BlockPos} of the block in the world
     * @param index the index of the tint layer
     * @return the RGB color of the tint as an integer (0xRRGGBB)
     */
    int apply(BlockState state, BlockAndTintGetter world, BlockPos pos, int index);
}