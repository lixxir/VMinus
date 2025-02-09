package net.lixir.vminus.mixins.entities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LookAtPlayerGoal.class)
public class LookAtPlayerGoalMixin {
    @Shadow
    protected Entity lookAt;

    @Inject(method = "canContinueToUse", at = @At("HEAD"), cancellable = true)
    private void injectAdditionalCondition(CallbackInfoReturnable<Boolean> cir) {
        if (lookAt != null && lookAt.isInvisible()) {
            cir.setReturnValue(false);
        }
    }
}