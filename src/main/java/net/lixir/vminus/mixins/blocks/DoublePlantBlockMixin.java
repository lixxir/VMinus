package net.lixir.vminus.mixins.blocks;

import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.RegistryEntryDefaults;
import net.lixir.vminus.registry.TintType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.DoublePlantBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DoublePlantBlock.class)
public class DoublePlantBlockMixin implements RegistryEntryDefaults<BlockEntry> {
	@Override
	public BlockEntry vminus$getDefault() {
		BlockEntry blockEntry = BlockEntry.of();
		blockEntry.tintType(TintType.GRASS);
		blockEntry.renderType(RenderType.cutout());
		blockEntry.model(BlockEntry.Model.DOUBLE_CROSS);
		return blockEntry;
	}
}
