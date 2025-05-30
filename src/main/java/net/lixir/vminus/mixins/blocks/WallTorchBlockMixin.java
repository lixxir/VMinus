package net.lixir.vminus.mixins.blocks;

import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.RegistryEntryDefaults;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.WallTorchBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WallTorchBlock.class)
public class WallTorchBlockMixin implements RegistryEntryDefaults<BlockEntry> {
	@Override
	public BlockEntry vminus$getDefault() {
		BlockEntry blockEntry = BlockEntry.of();
		blockEntry.renderType(RenderType.cutout());
		return blockEntry;
	}
}
