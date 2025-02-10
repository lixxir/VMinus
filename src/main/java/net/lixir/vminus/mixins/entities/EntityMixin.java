package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.core.conditions.VisionConditionArguments;
import net.lixir.vminus.core.visions.EntityVision;
import net.lixir.vminus.core.visions.ItemVision;
import net.lixir.vminus.core.visions.visionable.IEntityVisionable;
import net.lixir.vminus.registry.VMinusAttributes;
import net.lixir.vminus.util.ISpeedGetter;
import net.lixir.vminus.core.VisionProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements ISpeedGetter, IEntityVisionable {
    @Unique
    private final Entity vminus$entity = (Entity) (Object) this;
    @Unique
    private double vminus$speed = 0.0;

    @Inject(
            method = "setOldPosAndRot",
            at = @At("HEAD")
    )
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

    @Unique
    private EntityVision vminus$entityVision = new EntityVision();

    @Shadow
    public abstract EntityType<?> getType();

    @Shadow public abstract void tick();

    @Inject(method = "isSilent", at = @At("RETURN"), cancellable = true)
    private void isSilent(CallbackInfoReturnable<Boolean> cir) {
        float translucency = vminus$entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*2f;
        if (translucency >= 1f)
            cir.setReturnValue(true);
        Boolean value = vminus$getVision().silent.getValue(new VisionConditionArguments.Builder().passEntity(vminus$entity).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "dampensVibrations", at = @At("RETURN"), cancellable = true)
    private void dampensVibrations(CallbackInfoReturnable<Boolean> cir) {
        float translucency = vminus$entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*1.5f;
        if (translucency >= 1f)
            cir.setReturnValue(true);
        Boolean value = vminus$getVision().dampensVibrations.getValue(new VisionConditionArguments.Builder().passEntity(vminus$entity).build());
        if (value != null) cir.setReturnValue(value);
    }


    @Inject(method = "playStepSound", at = @At("RETURN"), cancellable = true)
    private void vminus$playStepSound(BlockPos p_20135_, BlockState p_20136_, CallbackInfo ci) {
        float translucency = vminus$entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*2f;
        if (translucency >= 1f)
            ci.cancel();
    }

    @Inject(method = "playSwimSound", at = @At("RETURN"), cancellable = true)
    private void vminus$playSwimSound(float p_20213_, CallbackInfo ci) {
        float translucency = vminus$entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*2f;
        if (translucency >= 1f)
            ci.cancel();
    }

    @Inject(method = "isInvisible", at = @At("RETURN"), cancellable = true)
    public void isInvisible(CallbackInfoReturnable<Boolean> cir) {
        float translucency = vminus$entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*1.5f;
        if (translucency >= 1f)
            cir.setReturnValue(true);
    }

    @ModifyArg(
            method = "playStepSound",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
            ),
            index = 1
    )
    private float vminus$playStepSound(float originalVolume) {
        float translucency = vminus$entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*2f;
        return originalVolume * (1.0F - translucency);
    }

    @ModifyArg(
            method = "playSwimSound",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
            ),
            index = 1
    )
    protected float vminus$playSwimSound(float originalVolume) {
        float translucency = vminus$entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*2f;
        return originalVolume * (1.0F - translucency);
    }


    @Override
    public void vminus$setVision(EntityVision vision) {
        this.vminus$entityVision = vision;
    }

    @Override
    public EntityVision vminus$getVision() {
        return this.vminus$entityVision;
    }
}
