package net.lixir.vminus.mixins.level;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {

    @Shadow @Nullable public abstract BlockState setBlockState(BlockPos p_62865_, BlockState p_62866_, boolean p_62867_);

    @Inject(method = "setBlockState", at = @At("HEAD"), cancellable = true)
    public final void vMinus$replaceBlockStates(BlockPos pos, @NotNull BlockState state, boolean p_62867_, CallbackInfoReturnable<BlockState> cir) {
        Block block = state.getBlock();
        Vision vision = Vision.get((VisionDuck) block);
        Boolean ban = vision.getValue(VisionProperties.Blocks.BAN, new VisionContext(state));
        Block replace = vision.getValue(VisionProperties.Blocks.REPLACE, new VisionContext(state));
        if (replace != null && !replace.equals(block)) {
            cir.setReturnValue(setBlockState(pos, replace.defaultBlockState(), p_62867_));
        } else if (ban != null && ban && !block.equals(Blocks.AIR)) {
            cir.setReturnValue(setBlockState(pos, Blocks.AIR.defaultBlockState(), p_62867_));
        }
    }
}
