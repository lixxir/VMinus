package net.lixir.vminus.api.datagen.block;

import net.lixir.vminus.api.registry.definition.BlockDefinition;
import net.lixir.vminus.api.rendertype.RenderTypeKey;
import net.lixir.vminus.api.tint.TintType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * Wrapper containing a {@link Block} and its
 * corresponding {@link BlockDefinition} for data generation processes.
 *
 * @param block           the block being processed
 * @param blockDefinition the definition associated with the block
 */
public record BlockData(@NotNull Block block, BlockDefinition blockDefinition) {

    /**
     * Creates a new {@link BlockData} instance from a block,
     * automatically resolving its {@link BlockDefinition}.
     *
     * @param block the block to wrap
     * @return a new {@link BlockData} instance containing the block and its definition
     */
    public static @NotNull BlockData of(Block block) {
        return new BlockData(block, BlockDefinition.of(block));
    }

    public @NotNull String modelTextureSuffix() {
        return blockDefinition.getModelTextureSuffix();
    }

    public @NotNull ResourceLocation modelTextureOverride() {
        return blockDefinition.getModelTextureOverride();
    }

    public @NotNull String renderType() {
        return blockDefinition.getRenderTypeKey().key();
    }

    public @NotNull TintType tintType() {
        return blockDefinition.getTintType();
    }
}