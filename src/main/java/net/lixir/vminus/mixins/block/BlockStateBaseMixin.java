package net.lixir.vminus.mixins.block;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionPropertyTypes;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.lixir.vminus.vision.util.VisionUtil;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin implements VisionDuck {
    @Shadow
    public abstract Block getBlock();

    @Unique
    private final BlockBehaviour.BlockStateBase vMinus$self = (BlockBehaviour.BlockStateBase) (Object) this;

    @Inject(method = "getLightEmission", at = @At("RETURN"), cancellable = true)
    private void vMinus$getLightEmission(CallbackInfoReturnable<Integer> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Blocks.LIGHT_LEVEL, new VisionContext(vMinus$self));
    }

    @Inject(method = "emissiveRendering", at = @At("RETURN"), cancellable = true)
    private void vMinus$emissiveRendering(CallbackInfoReturnable<Boolean> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Blocks.EMISSIVE, new VisionContext(vMinus$self));
    }

    @Inject(method = "canOcclude", at = @At("RETURN"), cancellable = true)
    private void vMinus$canOcclude(CallbackInfoReturnable<Boolean> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Blocks.OCCLUDE, new VisionContext(vMinus$self));
    }

    @Inject(method = "isRedstoneConductor", at = @At("RETURN"), cancellable = true)
    private void vMinus$isRedstoneConductor(CallbackInfoReturnable<Boolean> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Blocks.REDSTONE_CONDUCTOR, new VisionContext(vMinus$self));
    }

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void getDestroySpeed(BlockGetter p_60801_, BlockPos p_60802_, CallbackInfoReturnable<Float> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Blocks.HARDNESS, new VisionContext(vMinus$self));
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
