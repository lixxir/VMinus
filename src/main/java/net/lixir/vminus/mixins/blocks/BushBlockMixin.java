package net.lixir.vminus.mixins.blocks;

import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BushBlock.class)
public abstract class BushBlockMixin {
	@Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
	public void mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (blockState.is(VMinusTags.Blocks.CAN_SUSTAIN_PLANTS)) {
			cir.setReturnValue(true);
		}
	}
}
