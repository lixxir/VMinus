package net.lixir.vminus.mixins.blocks;

import net.lixir.vminus.core.conditions.VisionConditionArguments;
import net.lixir.vminus.core.visions.BlockVision;
import net.lixir.vminus.core.visions.visionable.IBlockVisionable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin  implements IBlockVisionable {
    @Unique
    private final BlockBehaviour.BlockStateBase vminus$blockBaseState = (BlockBehaviour.BlockStateBase) (Object) this;


    @Inject(method = "getLightEmission", at = @At("RETURN"), cancellable = true)
    private void getLightEmission(CallbackInfoReturnable<Integer> cir) {
        Integer value = vminus$getVision().lightLevel.getValue(new VisionConditionArguments.Builder().passBlockStateBase(vminus$blockBaseState).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "emissiveRendering", at = @At("RETURN"), cancellable = true)
    private void emissiveRendering(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = vminus$getVision().emissive.getValue(new VisionConditionArguments.Builder().passBlockStateBase(vminus$blockBaseState).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "canOcclude", at = @At("RETURN"), cancellable = true)
    private void canOcclude(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = vminus$getVision().occludes.getValue(new VisionConditionArguments.Builder().passBlockStateBase(vminus$blockBaseState).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "isRedstoneConductor", at = @At("RETURN"), cancellable = true)
    private void isRedstoneConductor(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = vminus$getVision().redstoneConductor.getValue(new VisionConditionArguments.Builder().passBlockStateBase(vminus$blockBaseState).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void getDestroySpeed(BlockGetter p_60801_, BlockPos p_60802_, CallbackInfoReturnable<Float> cir) {
        Float value = vminus$getVision().destroySpeed.getValue(new VisionConditionArguments.Builder().passBlockStateBase(vminus$blockBaseState).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Unique
    public BlockVision vminus$getVision() {
        if (vminus$blockBaseState.getBlock() instanceof IBlockVisionable iVisionable) {
            return iVisionable.vminus$getVision();
        }
        return null;
    }
}
