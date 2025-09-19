package net.lixir.vminus.mixins.level;

import net.lixir.vminus.api.block.util.BlockStateUtils;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunkSection.class)
public abstract class LevelChunkSectionMixin {
    @Shadow
    public abstract BlockState setBlockState(int x, int y, int z, BlockState state, boolean querySafety);


    @Inject(method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("HEAD"), cancellable = true)
    private void vMinus$replaceBlockStates(int x, int y, int z, @NotNull BlockState state, boolean querySafety, CallbackInfoReturnable<BlockState> cir) {
        Block block = state.getBlock();
        Vision vision = Vision.get((VisionDuck) block);
        Boolean ban = vision.getValue(VisionProperties.Blocks.BAN, new VisionContext(state));
        Block replace = vision.getValue(VisionProperties.Blocks.REPLACE, new VisionContext(state));
        if (replace != null && !replace.equals(block)) {
            BlockState newState = replace.defaultBlockState();
            newState = BlockStateUtils.copyProperties(state, newState);
            cir.setReturnValue(setBlockState(x, y, z, newState, querySafety));
        } else if (ban != null && ban && !block.equals(Blocks.AIR)) {
            cir.setReturnValue(setBlockState(x, y, z, Blocks.AIR.defaultBlockState(), querySafety));
        }
    }
}
