package net.lixir.vminus.mixins.blocks;

import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.RegistryEntryDefaults;
import net.lixir.vminus.registry.TintType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin implements RegistryEntryDefaults<BlockEntry, Block> {
	@Override
	public BlockEntry vminus$getDefault() {
		BlockEntry blockEntry = BlockEntry.of();
		blockEntry.renderType(RenderType.cutoutMipped());
		blockEntry.model(BlockEntry.Model.ALL_SIDED_CUBE);
		blockEntry.tintType(TintType.FOLIAGE);
		blockEntry.tags(List.of(BlockTags.LEAVES));
		return blockEntry;
	}
}
