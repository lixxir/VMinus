package net.lixir.vminus.mixins.command;

import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockStateArgument.class)
public interface BlockStateArgumentAccessor {
    @Accessor("blocks")
    HolderLookup<Block> getBlocks();
}