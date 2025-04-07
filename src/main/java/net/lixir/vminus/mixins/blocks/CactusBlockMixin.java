package net.lixir.vminus.mixins.blocks;

import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CactusBlock.class)
public abstract class CactusBlockMixin {
	@Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
	protected void canSurvive(BlockState p_51153_, LevelReader p_51154_, BlockPos p_51155_, CallbackInfoReturnable<Boolean> cir) {
		boolean stable = true;
		for(Direction direction : Direction.Plane.HORIZONTAL) {
			BlockState blockstate = p_51154_.getBlockState(p_51155_.relative(direction));
			if (blockstate.isSolid() || p_51154_.getFluidState(p_51155_.relative(direction)).is(FluidTags.LAVA)) {
				stable = false;
				break;
			}
		}
		BlockState blockstate1 = p_51154_.getBlockState(p_51155_.below());
		if (blockstate1.is(VMinusTags.Blocks.CAN_SUSTAIN_CACTUS) && stable)
			cir.setReturnValue(true);
	}


}
