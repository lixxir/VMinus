package net.lixir.vminus.mixins.level;

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
    public abstract BlockState setBlockState(int x, int y, int z, BlockState state);

    @Inject(method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("HEAD"), cancellable = true)
    public final void vMinus$replaceBlockStates(int x, int y, int z, @NotNull BlockState blockState, CallbackInfoReturnable<BlockState> cir) {
        Block block = blockState.getBlock();
        Vision vision = Vision.get((VisionDuck) block);
        Boolean ban = vision.getValue(VisionProperties.Blocks.BAN, new VisionContext(blockState));
        Block replace = vision.getValue(VisionProperties.Blocks.REPLACE, new VisionContext(blockState));
        if (replace != null && !replace.equals(block)) {
            cir.setReturnValue(setBlockState(x, y, z, replace.defaultBlockState()));
        } else if (ban != null && ban && !block.equals(Blocks.AIR)) {
            cir.setReturnValue(setBlockState(x, y, z, Blocks.AIR.defaultBlockState()));
        }
    }
}
