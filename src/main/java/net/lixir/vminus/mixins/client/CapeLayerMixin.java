package net.lixir.vminus.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.lixir.vminus.capes.CapeHelper;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
@OnlyIn(Dist.CLIENT)
public abstract class CapeLayerMixin extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public CapeLayerMixin(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> p_116602_) {
        super(p_116602_);
    }

    /**
     * Allowing for the capes resource location texture to be replaced
     *
     * @reason Epic
     * @author lixir
     */
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void render(PoseStack p_116615_, MultiBufferSource p_116616_, int p_116617_, AbstractClientPlayer p_116618_, float p_116619_, float p_116620_, float p_116621_, float p_116622_, float p_116623_, float p_116624_, CallbackInfo ci) {
        if (p_116618_.isCapeLoaded() && !p_116618_.isInvisible() && p_116618_.isModelPartShown(PlayerModelPart.CAPE)) {
            ResourceLocation capeTexture = CapeHelper.getCapeTexture(p_116618_);
            if (capeTexture != null) {
                ItemStack itemstack = p_116618_.getItemBySlot(EquipmentSlot.CHEST);
                if (!itemstack.is(Items.ELYTRA)) {
                    p_116615_.pushPose();
                    p_116615_.translate(0.0F, 0.0F, 0.125F);
                    double d0 = Mth.lerp(p_116621_, p_116618_.xCloakO, p_116618_.xCloak) - Mth.lerp(p_116621_, p_116618_.xo, p_116618_.getX());
                    double d1 = Mth.lerp(p_116621_, p_116618_.yCloakO, p_116618_.yCloak) - Mth.lerp(p_116621_, p_116618_.yo, p_116618_.getY());
                    double d2 = Mth.lerp(p_116621_, p_116618_.zCloakO, p_116618_.zCloak) - Mth.lerp(p_116621_, p_116618_.zo, p_116618_.getZ());
                    float f = Mth.rotLerp(p_116621_, p_116618_.yBodyRotO, p_116618_.yBodyRot);
                    double d3 = Mth.sin(f * ((float) Math.PI / 180F));
                    double d4 = -Mth.cos(f * ((float) Math.PI / 180F));
                    float f1 = (float) d1 * 10.0F;
                    f1 = Mth.clamp(f1, -6.0F, 32.0F);
                    float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
                    f2 = Mth.clamp(f2, 0.0F, 150.0F);
                    float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
                    f3 = Mth.clamp(f3, -20.0F, 20.0F);
                    if (f2 < 0.0F) {
                        f2 = 0.0F;
                    }
                    float f4 = Mth.lerp(p_116621_, p_116618_.oBob, p_116618_.bob);
                    f1 += Mth.sin(Mth.lerp(p_116621_, p_116618_.walkDistO, p_116618_.walkDist) * 6.0F) * 32.0F * f4;
                    if (p_116618_.isCrouching()) {
                        f1 += 25.0F;
                    }
                    p_116615_.mulPose(Axis.XP.rotationDegrees(6.0F + f2 / 2.0F + f1));
                    p_116615_.mulPose(Axis.ZP.rotationDegrees(f3 / 2.0F));
                    p_116615_.mulPose(Axis.YP.rotationDegrees(180.0F - f3 / 2.0F));
                    VertexConsumer vertexconsumer = p_116616_.getBuffer(RenderType.entitySolid(capeTexture));
                    this.getParentModel().renderCloak(p_116615_, vertexconsumer, p_116617_, OverlayTexture.NO_OVERLAY);
                    p_116615_.popPose();
                    ci.cancel();
                }
            }
        }
    }
}
