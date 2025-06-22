package net.lixir.vminus.mixins.block;

import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CactusBlock.class)
public abstract class CactusBlockMixin {
	@Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
	public final void vMinus$canSurvive(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			BlockState neighbor = level.getBlockState(pos.relative(direction));
			if (neighbor.isSolid() || level.getFluidState(pos.relative(direction)).is(FluidTags.LAVA)) {
				return;
			}
		}

		BlockState below = level.getBlockState(pos.below());

		boolean canSurvive = (
				below.canSustainPlant(level, pos.below(), Direction.UP, (IPlantable) this) ||
						below.is(VMinusTags.Blocks.CAN_SUSTAIN_CACTUS)
		) && !level.getBlockState(pos.above()).liquid();

		if (canSurvive) {
			cir.setReturnValue(true);
		}
	}

}
