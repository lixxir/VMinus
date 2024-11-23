package net.lixir.vminus.mixins.client.entityrenderers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Blaze;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.renderer.entity.BlazeRenderer.class)
public abstract class BlazeRendererMixin {
    @Inject(method = "getTextureLocation", at = @At("HEAD"), cancellable = true)
    private void injectGetTextureLocation(Blaze entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (entity.getPersistentData().contains("variant")) {
            String variant = entity.getPersistentData().getString("variant");
            ResourceLocation customTexture = new ResourceLocation("vminus:textures/entity/variants/blaze/" + variant + ".png");
            if (customTexture != null)
                cir.setReturnValue(customTexture);
        }
    }
}
