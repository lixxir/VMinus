package net.lixir.vminus.api.tint;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Default VMinus provided tint types.
 */
public enum BuiltInTintTypes implements TintType {
    FOLIAGE("foliage",
            (stack, tintIndex) -> tintIndex != null && tintIndex == 0 ? FoliageColor.getDefaultColor() : -1,
            (bs, world, pos, index) -> world != null && pos != null
                    ? BiomeColors.getAverageFoliageColor(world, pos)
                    : FoliageColor.getDefaultColor()
    ),
    GRASS("grass",
            (stack, tintIndex) -> tintIndex != null && tintIndex == 0 ? GrassColor.getDefaultColor() : -1,
            (bs, world, pos, index) -> world != null && pos != null
                    ? BiomeColors.getAverageGrassColor(world, pos)
                    : GrassColor.getDefaultColor()
    ),
    UNSET("unset", null, null),
    NONE("none", null, null);

    private final String name;
    private final ItemTintFunction itemTint;
    private final BlockTintFunction blockTint;

    BuiltInTintTypes(String name, ItemTintFunction itemTint, BlockTintFunction blockTint) {
        this.name = name;
        this.itemTint = itemTint;
        this.blockTint = blockTint;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable ItemTintFunction getItemTint() {
        return itemTint;
    }

    @Override
    public @Nullable BlockTintFunction getBlockTint() {
        return blockTint;
    }
}
