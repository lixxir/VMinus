package net.lixir.vminus.mixins.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {
    @Unique
    private LivingEntity vminus$capturedEntity;

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"))
    private void captureEntity(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, LivingEntity entity, float p1, float p2, float p3, float p4, float p5, float p6, CallbackInfo ci) {
        this.vminus$capturedEntity = entity;
    }

    @Inject(method = "renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/ArmorItem;Lnet/minecraft/client/model/Model;ZFFFLnet/minecraft/resources/ResourceLocation;)V", at = @At("HEAD"), remap = false, cancellable = true)
    private void renderModel(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, ArmorItem armorItem, Model model, boolean flag, float r, float g, float b, ResourceLocation armorResource, CallbackInfo ci) {
        if (vminus$capturedEntity == null)
            return;
        float translucency = vminus$capturedEntity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY);
        float changedTranslucency = 1f - translucency;
        if (changedTranslucency != 1f) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.itemEntityTranslucentCull(armorResource));
            model.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, r, g, b, changedTranslucency);
            ci.cancel();
        }
    }
}