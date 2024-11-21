package net.lixir.vminus.mixins;

import com.google.gson.JsonObject;
import net.lixir.vminus.JsonValueUtil;
import net.lixir.vminus.VisionHandler;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityRealMixin {
    @Unique
    private final Entity entity = (Entity) (Object) this;

    @Inject(method = "isSilent", at = @At("HEAD"), cancellable = true)
    private void isSilent(CallbackInfoReturnable<Boolean> cir) {
        JsonObject visionData = VisionHandler.getVisionData(entity);
        if (visionData != null && visionData.has("silent")) {
            cir.setReturnValue(JsonValueUtil.isBooleanMet(visionData, "silent", entity));
        }
    }

    @Inject(method = "dampensVibrations", at = @At("HEAD"), cancellable = true)
    private void dampensVibrations(CallbackInfoReturnable<Boolean> cir) {
        JsonObject visionData = VisionHandler.getVisionData(entity);
        if (visionData != null && visionData.has("dampens_vibrations")) {
            cir.setReturnValue(JsonValueUtil.isBooleanMet(visionData, "dampens_vibrations", entity));
        }
    }
}
