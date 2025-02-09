package net.lixir.vminus.mixins;

import net.lixir.vminus.core.VisionProperties;
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
    private final MobEffect vminus$effect = (MobEffect) (Object) this;

    @Inject(method = "getColor", at = @At("RETURN"), cancellable = true)
    private void getColor(CallbackInfoReturnable<Integer> cir) {
        String colorString = VisionProperties.getString(VisionProperties.Names.COLOR, vminus$effect);
        if (colorString != null && !colorString.isEmpty()) {
            if (colorString.startsWith("#"))
                colorString = colorString.substring(1);
            int colorInt = Integer.parseInt(colorString, 16);
            cir.setReturnValue(colorInt);
        }
    }

    @Inject(method = "getCategory", at = @At("RETURN"), cancellable = true)
    public void getCategory(CallbackInfoReturnable<MobEffectCategory> cir) {
        String categoryString = VisionProperties.getString(VisionProperties.Names.CATEGORY, vminus$effect);
        if (categoryString != null && !categoryString.isEmpty()) {
            MobEffectCategory customCategory = MobEffectCategory.valueOf(categoryString);
            cir.setReturnValue(customCategory);
        }
    }
}
