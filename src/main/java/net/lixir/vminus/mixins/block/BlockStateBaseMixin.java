package net.lixir.vminus.mixins.block;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionPropertyTypes;
import net.lixir.vminus.vision.util.VisionUtil;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
    public final void vMinus$getLightEmission(CallbackInfoReturnable<Integer> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Blocks.LIGHT_LEVEL, new VisionContext(vMinus$self));
    }

    @Inject(method = "emissiveRendering", at = @At("RETURN"), cancellable = true)
    public final void vMinus$emissiveRendering(CallbackInfoReturnable<Boolean> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Blocks.EMISSIVE, new VisionContext(vMinus$self));
    }

    @Inject(method = "canOcclude", at = @At("RETURN"), cancellable = true)
    public final void vMinus$canOcclude(CallbackInfoReturnable<Boolean> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Blocks.OCCLUDE, new VisionContext(vMinus$self));
    }

    @Inject(method = "isRedstoneConductor", at = @At("RETURN"), cancellable = true)
    public final void vMinus$isRedstoneConductor(CallbackInfoReturnable<Boolean> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Blocks.REDSTONE_CONDUCTOR, new VisionContext(vMinus$self));
    }

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void getDestroySpeed(BlockGetter p_60801_, BlockPos p_60802_, CallbackInfoReturnable<Float> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Blocks.HARDNESS, new VisionContext(vMinus$self));
    }

    @Override
    public int vMinus$getVisionIndex() {
        return ((VisionDuck) getBlock()).vMinus$getVisionIndex();
    }
}
