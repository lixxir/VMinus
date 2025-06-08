package net.lixir.vminus.mixins.blocks;

import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.RegistryEntryDefaults;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(AbstractGlassBlock.class)
public class AbstractGlassBlockMixin implements RegistryEntryDefaults<BlockEntry, Block> {
	@Override
	public BlockEntry vminus$getDefault() {
		BlockEntry blockEntry = BlockEntry.of();
		blockEntry.renderType(RenderType.cutout());
		blockEntry.model(BlockEntry.Model.ALL_SIDED_CUBE);
		blockEntry.tags(List.of(Tags.Blocks.GLASS));
		return blockEntry;
	}
}
