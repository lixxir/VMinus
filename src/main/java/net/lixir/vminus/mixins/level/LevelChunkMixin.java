package net.lixir.vminus.mixins.level;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {
    @Shadow @Nullable public abstract BlockState setBlockState(BlockPos p_62865_, BlockState p_62866_, boolean p_62867_);

    @Unique
    private LevelChunk vMinus$self = (LevelChunk) (Object) this;

    /*
    @Inject(method = "setBlockState", at = @At("HEAD"), cancellable = true)
    private void vMinus$setBlockState(BlockPos pos, BlockState state, boolean safeCheck, CallbackInfoReturnable<BlockState> cir) {
        Block block = state.getBlock();
        Boolean ban = Vision.getValue(state, VisionProperties.Blocks.BAN);
        Block replace = Vision.getValue(state, VisionProperties.Blocks.REPLACE);

        if (replace != null && !replace.equals(block)) {
            BlockState replaceState = replace.defaultBlockState();
            BlockState newState = setBlockState(pos, replaceState, safeCheck);
            if (newState != null)
                vMinus$self.getLevel().sendBlockUpdated(pos, state, newState, 2);
            cir.setReturnValue(newState);

            return;
        }

        if (ban != null && ban && !block.equals(Blocks.AIR)) {
            BlockState replaceState = Blocks.AIR.defaultBlockState();
            BlockState newState = setBlockState(pos, replaceState, safeCheck);
            if (newState != null)
                vMinus$self.getLevel().sendBlockUpdated(pos, state, newState, 2);
            cir.setReturnValue(newState);
        }
    }

     */
}
