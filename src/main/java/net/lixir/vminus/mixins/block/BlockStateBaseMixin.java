package net.lixir.vminus.mixins.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.vision.*;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin implements VisionDuck {
    @Shadow
    public abstract Block getBlock();

    @ModifyReturnValue(method = "getLightEmission", at = @At("RETURN"))
    private int vMinus$getLightEmission(int original) {
        return Vision.getValue(getBlock(), VisionProperties.Blocks.LIGHT_LEVEL, original);
    }

    @ModifyReturnValue(method = "emissiveRendering", at = @At("RETURN"))
    private boolean vMinus$emissiveRendering(boolean original) {
        return Vision.getValue(getBlock(), VisionProperties.Blocks.EMISSIVE, original);
    }

    @ModifyReturnValue(method = "canOcclude", at = @At("RETURN"))
    private boolean vMinus$canOcclude(boolean original) {
        return Vision.getValue(getBlock(), VisionProperties.Blocks.OCCLUDE, original);
    }

    @ModifyReturnValue(method = "isRedstoneConductor", at = @At("RETURN"))
    private boolean vMinus$isRedstoneConductor(boolean original) {
        return Vision.getValue(getBlock(), VisionProperties.Blocks.REDSTONE_CONDUCTOR, original);
    }

    @ModifyReturnValue(method = "getDestroySpeed", at = @At("RETURN"))
    private float getDestroySpeed(float original) {
        return Vision.getValue(getBlock(), VisionProperties.Blocks.BREAK_SPEED, original);
    }

    @Override
    public @NonNull VisionType<?> vMinus$getVisionType() {
        return VisionTypes.BLOCK;
    }

    @Override
    public @Nullable ResourceLocation vMinus$getVisionId() {
        return ((VisionDuck) getBlock()).vMinus$getVisionId();
    }
}
