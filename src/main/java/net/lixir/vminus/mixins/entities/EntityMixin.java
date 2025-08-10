package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.util.ISpeedGetter;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements VisionDuck, ISpeedGetter {
    @Shadow
    public abstract EntityType<?> getType();

    @Unique
    private final Entity vMinus$self = (Entity) (Object) this;

    @Unique
    private double vMinus$speed = 0.0;

    @Override
    public @NonNull VisionType<?> vMinus$getVisionType() {
        return VisionTypes.ENTITY;
    }

    @Override
    public @Nullable ResourceLocation vMinus$getVisionId() {
        return ((VisionDuck) getType()).vMinus$getVisionId();
    }

    @Inject(method = "setOldPosAndRot", at = @At("HEAD"))
    private void vminus$setOldPosAndRot(CallbackInfo ci) {
        var self = (Entity) (Object) this;
        var deltaX = self.getX() - self.xOld;
        var deltaZ = self.getZ() - self.zOld;
        vMinus$speed = deltaX * deltaX + deltaZ * deltaZ;
    }

    @Unique
    public double vMinus$getSpeed() {
        return vMinus$speed;
    }

    @Inject(method = "isSilent", at = @At("RETURN"), cancellable = true)
    private void vMinus$isSilent(CallbackInfoReturnable<Boolean> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Entities.SILENT, new VisionContext(vMinus$self));
    }


    @Inject(method = "dampensVibrations", at = @At("RETURN"), cancellable = true)
    private void vMinus$dampensVibrations(CallbackInfoReturnable<Boolean> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Entities.DAMPENS_VIBRATION, new VisionContext(vMinus$self));
    }
}
