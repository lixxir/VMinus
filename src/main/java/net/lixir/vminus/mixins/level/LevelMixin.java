package net.lixir.vminus.mixins.level;

import net.lixir.vminus.block.VBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class LevelMixin {
    @Unique
    private final Level vminus$level = (Level) (Object) this;

    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z", at = @At(value = "TAIL",
            target = "Lnet/minecraft/world/level/chunk/LevelChunk;setBlockState(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;"))
    private void detour$setBlock(BlockPos blockPos, BlockState blockState, int p_46607_, int p_46608_, @NotNull CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue())
            return;
        if (vminus$level.isClientSide() || !(vminus$level instanceof ServerLevel serverLevel))
            return;
        if (blockState.isAir())
            return;
        Block block = blockState.getBlock();
        if (block instanceof VBlock vBlock && vBlock.shouldNaturallyScheduleTick() &&!serverLevel.getBlockTicks().hasScheduledTick(blockPos, block)) {
            serverLevel.scheduleTick(blockPos, block, 1);
        }
    }
}
