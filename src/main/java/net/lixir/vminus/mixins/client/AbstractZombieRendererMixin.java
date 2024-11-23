package net.lixir.vminus.mixins.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.model.ZombieModel;

@Mixin(AbstractZombieRenderer.class)
public abstract class AbstractZombieRendererMixin<T extends Zombie, M extends ZombieModel<T>> extends HumanoidMobRenderer<T, M> {
    public AbstractZombieRendererMixin(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation("textures/entity/zombie/zombie.png");

    @Inject(method = "getTextureLocation", at = @At("HEAD"), cancellable = true)
    private void injectGetTextureLocation(Zombie zombie, CallbackInfoReturnable<ResourceLocation> cir) {
        if (zombie.getPersistentData().contains("variant")) {
            String variant = zombie.getPersistentData().getString("variant");
            if (variant.equals("normal")) {
                cir.setReturnValue(ZOMBIE_LOCATION);
            } else {
                ResourceLocation customTexture = new ResourceLocation("vminus:textures/entity/variants/zombie/" + variant + ".png");
                if (customTexture != null)
                    cir.setReturnValue(customTexture);
            }
        } else {
            cir.setReturnValue(ZOMBIE_LOCATION);
        }
    }
}
