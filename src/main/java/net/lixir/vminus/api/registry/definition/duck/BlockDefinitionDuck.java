package net.lixir.vminus.api.registry.definition.duck;

import net.lixir.vminus.api.registry.definition.BlockDefinition;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

/**
 * A duck interface for attaching a {@link BlockDefinition} to a {@link Block}.
 * <p>
 * This allows VMinus to store metadata or definition information on blocks without
 * modifying the original {@link Block} class.
 */
public interface BlockDefinitionDuck extends RegistryDefinitionDuck<BlockDefinition, Block> {
    @Override
    void vMinus$setDefinition(@Nullable BlockDefinition blockDefinition);

    @Nullable
    @Override
    BlockDefinition vMinus$getDefinition();

    static BlockDefinitionDuck of(Block block) {
        return (BlockDefinitionDuck) block;
    }
}
