package net.lixir.vminus.mixins;

import com.google.gson.JsonObject;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHandler;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffect.class)
public abstract class MobEffectMixin {
    @Unique
    private final MobEffect effect = (MobEffect) (Object) this;

    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private void getColor(CallbackInfoReturnable<Integer> cir) {
        JsonObject visionData = VisionHandler.getVisionData(effect);
        if (visionData != null && visionData.has("color")) {
            String color = VisionValueHandler.getFirstValidString(visionData, "color");
            if (color.startsWith("#")) {
                int colorInt = Integer.parseInt(color.substring(1), 16);
                cir.setReturnValue(colorInt);
            } else {
                VMinusMod.LOGGER.warn("Mob Effect color must begin with a \"#\": " + effect);
            }
        }
    }

    @Inject(method = "getCategory", at = @At("HEAD"), cancellable = true)
    public void getCategory(CallbackInfoReturnable<MobEffectCategory> cir) {
        JsonObject visionData = VisionHandler.getVisionData(effect);
        if (visionData != null && visionData.has("category")) {
            String category = VisionValueHandler.getFirstValidString(visionData, "category");
            MobEffectCategory customCategory = getCategoryFromString(category);
            if (customCategory != null) {
                cir.setReturnValue(customCategory);
            }
        }
    }

    @Unique
    private MobEffectCategory getCategoryFromString(String category) {
        switch (category.toLowerCase()) {
            case "harmful":
                return MobEffectCategory.HARMFUL;
            case "beneficial":
                return MobEffectCategory.BENEFICIAL;
            case "neutral":
                return MobEffectCategory.NEUTRAL;
            default:
                VMinusMod.LOGGER.warn("Unknown Mob Effect Category: " + category);
        }
        return null;
    }
}
