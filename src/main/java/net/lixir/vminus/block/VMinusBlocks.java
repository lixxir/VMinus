package net.lixir.vminus.block;

import net.lixir.vminus.registry.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static net.lixir.vminus.VMinus.REGISTRY;

public class VMinusBlocks {
    public static void init() {}
    public static final Block TEST = REGISTRY.block("test", new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)), BlockEntry.of().tag(BlockTags.ACACIA_LOGS));
}
