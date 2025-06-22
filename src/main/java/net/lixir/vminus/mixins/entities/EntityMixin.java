package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.util.ISpeedGetter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements VisionDuck, ISpeedGetter {
    @Shadow
    public abstract EntityType<?> getType();

    @Unique
    private final Entity vminus$entity = (Entity) (Object) this;

    @Unique
    private double vminus$speed = 0.0;

    @Override
    public int vMinus$getVisionIndex() {
        return ((VisionDuck) getType()).vMinus$getVisionIndex();
    }

    @Inject(method = "setOldPosAndRot", at = @At("HEAD"))
    private void vminus$setOldPosAndRot(CallbackInfo ci) {
        var self = (Entity) (Object) this;
        var deltaX = self.getX() - self.xOld;
        var deltaZ = self.getZ() - self.zOld;
        vminus$speed = deltaX * deltaX + deltaZ * deltaZ;
    }

    @Unique
    public double vminus$getSpeed() {
        return vminus$speed;
    }



    /*

    @Inject(method = "isSilent", at = @At("RETURN"), cancellable = true)
    private void isSilent(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = vminus$getVision().silent.value(new VisionContext.Builder().pass(vminus$entity).build());
        if (value != null)
            cir.setReturnValue(value);
    }

    @Inject(method = "dampensVibrations", at = @At("RETURN"), cancellable = true)
    private void dampensVibrations(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = vminus$getVision().dampens_vibration.value(new VisionContext.Builder().pass(vminus$entity).build());
        if (value != null)
            cir.setReturnValue(value);
    }

     */


}
