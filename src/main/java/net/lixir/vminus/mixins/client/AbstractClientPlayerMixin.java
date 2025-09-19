package net.lixir.vminus.mixins.client;

import com.mojang.authlib.GameProfile;
import net.lixir.vminus.cape.Cape;
import net.lixir.vminus.world.item.IMaxDurationGetter;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player {
    public AbstractClientPlayerMixin(Level level, BlockPos blockPos, float p_251702_, GameProfile gameProfile) {
        super(level, blockPos, p_251702_, gameProfile);
    }

    @Shadow
    @Nullable
    protected abstract PlayerInfo getPlayerInfo();

    @Inject(method = "getElytraTextureLocation", at = @At("RETURN"), cancellable = true)
    private void vMinus$getElytraTextureLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        vMinus$trySetCapeTexture(cir);
    }

    @Inject(method = "getCloakTextureLocation", at = @At("RETURN"), cancellable = true)
    private void vMinus$getCloakTextureLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        vMinus$trySetCapeTexture(cir);
    }

    @Inject(method = "isCapeLoaded", at = @At("RETURN"), cancellable = true)
    private void vMinus$isCapeLoaded(CallbackInfoReturnable<Boolean> cir) {
        if (vMinus$hasCustomCape()) {
            cir.setReturnValue(true);
        }
    }


    @ModifyConstant(
            method = "getFieldOfViewModifier",
            constant = @Constant(floatValue = 20.0F)
    )
    private float vMinus$getFieldOfViewModifier(float original) {
        ItemStack itemstack = this.getUseItem();
        return ((IMaxDurationGetter) itemstack.getItem()).vminus$getMaxDuration();
    }

    @Unique
    private boolean vMinus$hasCustomCape() {
        PlayerInfo playerInfo = this.getPlayerInfo();
        if (playerInfo != null) {
            AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
            return Cape.getCapeTexture(player) != null;
        }
        return false;
    }

    @Unique
    private void vMinus$trySetCapeTexture(CallbackInfoReturnable<ResourceLocation> cir) {
        if (vMinus$hasCustomCape()) {
            AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
            ResourceLocation customCapeTexture = Cape.getCapeTexture(player);
            if (customCapeTexture != null) {
                cir.setReturnValue(customCapeTexture);
            }
        }
    }
}
