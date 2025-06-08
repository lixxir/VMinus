package net.lixir.vminus.mixins.blocks;

import net.lixir.vminus.registry.util.VMinusTags;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.RegistryEntryDefaults;
import net.lixir.vminus.registry.TintType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BushBlock.class)
public class BushBlockMixin implements RegistryEntryDefaults<BlockEntry, Block> {
	@Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
	public void mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (blockState.is(VMinusTags.Blocks.CAN_SUSTAIN_PLANTS)) {
			cir.setReturnValue(true);
		}
	}

	@Override
	public BlockEntry vminus$getDefault() {
		BlockEntry blockEntry = BlockEntry.of();
		blockEntry.tintType(TintType.GRASS);
		blockEntry.renderType(RenderType.cutout());
		blockEntry.model(BlockEntry.Model.CROSS);
		return blockEntry;
	}
}
