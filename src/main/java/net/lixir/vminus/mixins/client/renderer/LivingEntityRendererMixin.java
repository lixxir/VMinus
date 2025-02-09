package net.lixir.vminus.mixins.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
    @Unique
    private final LivingEntityRenderer vminus$livingEntityRenderer = (LivingEntityRenderer) (Object) this;
    @Unique
    private T vminus$storedEntity;
    @Unique
    private static final float vminus$EPSILON = 1.0E-6F;

    @Inject(method = "render*", at = @At("HEAD"))
    private void captureEntity(T entity, float p_115456_, float p_115457_, PoseStack p_115458_, net.minecraft.client.renderer.MultiBufferSource p_115459_, int p_115460_, CallbackInfo ci) {
        this.vminus$storedEntity = entity;
    }

    @Unique
    @Redirect(method = "render*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
    private void renderColorChangedModel(EntityModel model, PoseStack poseStack, VertexConsumer vertexConsumer, int i, int overlay, float red, float green, float blue, float alpha) {
        float translucency = Mth.clamp(vminus$storedEntity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*1.5f, 0f, 1f);
        float colorDrain = Mth.clamp(translucency*2f, 0f, 1f);
        model.renderToBuffer(poseStack, vertexConsumer, i, overlay, red- (red * colorDrain), green- (green * colorDrain), blue- (blue * colorDrain), alpha - (alpha * translucency));
    }


    @Inject(method = "isBodyVisible", at = @At("RETURN"), cancellable = true)
    protected void isBodyVisible(T p_115341_, CallbackInfoReturnable<Boolean> cir) {
        float translucency =  Mth.clamp(vminus$storedEntity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*1.5f, 0f ,1f);
        if (translucency >= 1f)
            cir.setReturnValue(false);
    }


    @Inject(method = "getRenderType", at = @At("RETURN"), cancellable = true)
    private void onGetRenderType(T p_115322_, boolean p_115323_, boolean p_115324_, boolean p_115325_, CallbackInfoReturnable<RenderType> cir) {
        RenderType original = cir.getReturnValue();
        if (original != null) {
            float translucency = vminus$storedEntity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*1.5f;
            if (Math.abs(translucency) > vminus$EPSILON) {
                RenderType customType = RenderType.itemEntityTranslucentCull(vminus$livingEntityRenderer.getTextureLocation(vminus$storedEntity));
                cir.setReturnValue(customType);
            }
        }
    }
}
