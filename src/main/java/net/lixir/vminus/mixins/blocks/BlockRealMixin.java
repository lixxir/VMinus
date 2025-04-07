package net.lixir.vminus.mixins.blocks;

import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.BlockVision;
import net.lixir.vminus.visions.accessors.IBlockVisionAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockRealMixin implements IBlockVisionAccessor {
    @Unique
    private final Block vminus$block = (Block) (Object) this;

    @Unique
    private BlockVision vminus$blockVision = null;

    @Inject(method = "getSpeedFactor", at = @At("RETURN"), cancellable = true)
    private void getSpeedFactor(CallbackInfoReturnable<Float> cir) {
        Float value = vminus$getVision().speed_boost.value(new VisionConditionArguments(vminus$block));
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getFriction", at = @At("RETURN"), cancellable = true)
    private void getFriction(CallbackInfoReturnable<Float> cir) {
        Float value = vminus$getVision().friction.value(new VisionConditionArguments(vminus$block));
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getJumpFactor", at = @At("RETURN"), cancellable = true)
    private void getJumpFactor(CallbackInfoReturnable<Float> cir) {
        Float value = vminus$getVision().jump_boost.value(new VisionConditionArguments(vminus$block));
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getExplosionResistance", at = @At("RETURN"), cancellable = true)
    private void getExplosionResistance(CallbackInfoReturnable<Float> cir) {
        Float value = vminus$getVision().blast_resistance.value(new VisionConditionArguments(vminus$block));
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getSoundType", at = @At("RETURN"), cancellable = true)
    private void getSoundType(BlockState state, CallbackInfoReturnable<SoundType> cir) {
        SoundType value = vminus$getVision().sound.value(new VisionConditionArguments(vminus$block));
        if (value != null) cir.setReturnValue(value);
    }

    @Override
    public BlockVision vminus$getVision() {
        if (vminus$blockVision == null)
            return BlockVision.EMPTY;
        return vminus$blockVision;
    }

    @Override
    public void vminus$setVision(BlockVision vision) {
        if (vminus$blockVision == null)
            this.vminus$blockVision = vision;
        else
            this.vminus$blockVision.merge(vision);
    }
}
