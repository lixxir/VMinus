package net.lixir.vminus.mixins.blocks;

import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.RegistryEntryDefaults;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.FlowerBlock;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(FlowerBlock.class)
public class FlowerBlockMixin implements RegistryEntryDefaults<BlockEntry> {
	@Override
	public BlockEntry vminus$getDefault() {
		BlockEntry blockEntry = BlockEntry.of();
		blockEntry.renderType(RenderType.cutout());
		blockEntry.model(BlockEntry.Model.CROSS);
		blockEntry.tags(List.of(BlockTags.SMALL_FLOWERS));
		return blockEntry;
	}
}
