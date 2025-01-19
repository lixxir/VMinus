package net.lixir.vminus.mixins.client;

import net.lixir.vminus.capes.CapeHelper;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(AbstractClientPlayer.class)
@OnlyIn(Dist.CLIENT)
public abstract class AbstractClientPlayerMixin {
    @Shadow
    @Nullable
    protected abstract PlayerInfo getPlayerInfo();

    @Inject(method = "getElytraTextureLocation", at = @At("HEAD"), cancellable = true)
    private void vminus$getElytraTextureLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        PlayerInfo playerInfo = this.getPlayerInfo();
        if (playerInfo != null) {
            AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
            ResourceLocation customCapeTexture = CapeHelper.getCapeTexture(player);
            if (customCapeTexture != null) {
                cir.setReturnValue(customCapeTexture);
            }
        }
    }

    @Inject(method = "getCloakTextureLocation", at = @At("HEAD"), cancellable = true)
    private void vminus$getCloakTextureLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        PlayerInfo playerInfo = this.getPlayerInfo();
        if (playerInfo != null) {
            AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
            ResourceLocation customCapeTexture = CapeHelper.getCapeTexture(player);
            if (customCapeTexture != null) {
                cir.setReturnValue(customCapeTexture);
            }
        }
    }
}
