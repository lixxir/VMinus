package net.lixir.vminus.mixins.blocks;

import net.lixir.vminus.core.VisionProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    @Unique
    private final BlockBehaviour.BlockStateBase vminus$state = (BlockBehaviour.BlockStateBase) (Object) this;

    @Inject(method = "getLightEmission", at = @At("RETURN"), cancellable = true)
    private void getLightEmission(CallbackInfoReturnable<Integer> cir) {
        if (VisionProperties.searchElement(VisionProperties.Names.LIGHT_LEVEL, vminus$state) != null)
            cir.setReturnValue(Mth.clamp(VisionProperties.getNumber(VisionProperties.Names.LIGHT_LEVEL, vminus$state, cir.getReturnValue()).intValue(), 0, 16));
    }

    @Inject(method = "emissiveRendering", at = @At("RETURN"), cancellable = true)
    private void emissiveRendering(CallbackInfoReturnable<Boolean> cir) {
        if (VisionProperties.searchElement(VisionProperties.Names.EMISSIVE, vminus$state) != null)
            cir.setReturnValue(VisionProperties.getBoolean(VisionProperties.Names.EMISSIVE, vminus$state, cir.getReturnValue()));
    }

    @Inject(method = "canOcclude", at = @At("RETURN"), cancellable = true)
    private void canOcclude(CallbackInfoReturnable<Boolean> cir) {
        if (VisionProperties.searchElement(VisionProperties.Names.OCCLUDES, vminus$state) != null)
            cir.setReturnValue(VisionProperties.getBoolean(VisionProperties.Names.OCCLUDES, vminus$state, cir.getReturnValue()));
    }

    @Inject(method = "isRedstoneConductor", at = @At("RETURN"), cancellable = true)
    private void isRedstoneConductor(CallbackInfoReturnable<Boolean> cir) {
        if (VisionProperties.searchElement(VisionProperties.Names.CONDUCTOR, vminus$state) != null)
            cir.setReturnValue(VisionProperties.getBoolean(VisionProperties.Names.CONDUCTOR, vminus$state, cir.getReturnValue()));
    }

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void getDestroySpeed(BlockGetter p_60801_, BlockPos p_60802_, CallbackInfoReturnable<Float> cir) {
        if (VisionProperties.searchElement(VisionProperties.Names.DESTROY_TIME, vminus$state) != null)
            cir.setReturnValue(Math.max(-1, VisionProperties.getNumber(VisionProperties.Names.DESTROY_TIME, vminus$state, cir.getReturnValue()).floatValue()));
    }

    @Inject(method = "isValidSpawn", at = @At("RETURN"), cancellable = true)
    private void isValidSpawn(CallbackInfoReturnable<Boolean> cir) {
        if (VisionProperties.searchElement(VisionProperties.Names.VALID_SPAWN, vminus$state) != null)
            cir.setReturnValue(VisionProperties.getBoolean(VisionProperties.Names.VALID_SPAWN, vminus$state, cir.getReturnValue()));
    }
}
