package net.lixir.vminus.mixins.block;

import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CocoaBlock.class)
public abstract class CocaoBlockMixin {
    // Makes the blocks that cocao can survive on tag dependant instead of being specific to Jungle Logs.
    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    public final void vMinus$canSurvive(@NotNull BlockState blockState, @NotNull LevelReader level, @NotNull BlockPos blockPos, @NotNull CallbackInfoReturnable<Boolean> cir) {
        BlockState oppositeState = level.getBlockState(blockPos.relative(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)));
        cir.setReturnValue(oppositeState.is(VMinusTags.Blocks.COCAO_PLANTABLE_ON));
    }
}
