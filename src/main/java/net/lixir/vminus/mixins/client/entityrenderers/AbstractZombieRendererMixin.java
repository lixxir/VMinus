package net.lixir.vminus.mixins.client.entityrenderers;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.resources.ResourceLocation;

@Mixin(net.minecraft.client.renderer.entity.AbstractZombieRenderer.class)
public abstract class AbstractZombieRendererMixin {
    @Inject(method = "getTextureLocation", at = @At("HEAD"), cancellable = true)
    private void injectGetTextureLocation(Zombie zombie, CallbackInfoReturnable<ResourceLocation> cir) {
        if (zombie.getPersistentData().contains("variant")) {
            String variant = zombie.getPersistentData().getString("variant");
            ResourceLocation customTexture = new ResourceLocation("vminus:textures/entity/variants/zombie/" + variant + ".png");
            if (customTexture != null)
                    cir.setReturnValue(customTexture);
        }
    }
}
