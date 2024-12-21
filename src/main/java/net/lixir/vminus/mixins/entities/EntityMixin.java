package net.lixir.vminus.mixins.entities;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHandler;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Unique
    private final Entity entity = (Entity) (Object) this;

    @Inject(method = "isSilent", at = @At("HEAD"), cancellable = true)
    private void isSilent(CallbackInfoReturnable<Boolean> cir) {
        JsonObject visionData = VisionHandler.getVisionData(entity.getType());
        if (visionData != null && visionData.has("silent")) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(visionData, "silent", entity.getType()));
        }
    }

    @Inject(method = "dampensVibrations", at = @At("HEAD"), cancellable = true)
    private void dampensVibrations(CallbackInfoReturnable<Boolean> cir) {
        JsonObject visionData = VisionHandler.getVisionData(entity.getType());
        if (visionData != null && visionData.has("dampens_vibrations")) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(visionData, "dampens_vibrations", entity.getType()));
        }
    }
}
