package net.lixir.vminus.mixins.client;

import com.mojang.authlib.GameProfile;
import net.lixir.vminus.capes.CapeHelper;
import net.lixir.vminus.item.MaxDurationGetter;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player {
    public AbstractClientPlayerMixin(Level p_250508_, BlockPos p_250289_, float p_251702_, GameProfile p_252153_) {
        super(p_250508_, p_250289_, p_251702_, p_252153_);
    }

    @Shadow
    @Nullable
    protected abstract PlayerInfo getPlayerInfo();

    @Inject(method = "getElytraTextureLocation", at = @At("RETURN"), cancellable = true)
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

    @ModifyConstant(
            method = "getFieldOfViewModifier",
            constant = @Constant(floatValue = 20.0F)
    )
    private float modifyBowChargeTicks(float original) {
        ItemStack itemstack = this.getUseItem();
        return ((MaxDurationGetter) itemstack.getItem()).vminus$getMaxDuration();
    }

    @Inject(method = "getCloakTextureLocation", at = @At("RETURN"), cancellable = true)
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
