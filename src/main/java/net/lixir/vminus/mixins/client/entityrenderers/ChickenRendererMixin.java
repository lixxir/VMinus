package net.lixir.vminus.mixins.client.entityrenderers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.renderer.entity.ChickenRenderer.class)
public abstract class ChickenRendererMixin {
    @Inject(method = "getTextureLocation", at = @At("HEAD"), cancellable = true)
    private void injectGetTextureLocation(Chicken entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (entity.getPersistentData().contains("variant")) {
            String variant = entity.getPersistentData().getString("variant");
            ResourceLocation customTexture = new ResourceLocation("vminus:textures/entity/variants/chicken/" + variant + ".png");
            if (customTexture != null)
                cir.setReturnValue(customTexture);
        }
    }
}
