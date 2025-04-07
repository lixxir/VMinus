package net.lixir.vminus.mixins.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lixir.vminus.util.EntityVariantUtil;
import net.lixir.vminus.util.IEntityVariantAccessor;
import net.lixir.vminus.util.SizeAttributeUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity> extends EntityRenderer<T> {
    @Unique
    private final LivingEntityRenderer vminus$livingEntityRenderer = (LivingEntityRenderer) (Object) this;

    @Unique
    private Float vminus$initialShadowRadius = null;

    protected LivingEntityRendererMixin(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Inject(method = "scale", at = @At("HEAD"))
    protected void scale(T entity, PoseStack poseStack, float p_115316_, CallbackInfo ci) {
        float width = SizeAttributeUtil.getWidth(entity);
        float height =  SizeAttributeUtil.getHeight(entity);
        if (height != 1 || width != 1)
            poseStack.scale(width, height, width);
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    protected void scale(T entity, float p_115309_, float p_115310_, PoseStack poseStack, MultiBufferSource p_115312_, int p_115313_, CallbackInfo ci) {
        float width = SizeAttributeUtil.getWidth(entity);
        if (vminus$initialShadowRadius == null && this.shadowRadius != 0)
            this.vminus$initialShadowRadius = shadowRadius;
        if (vminus$initialShadowRadius != null && vminus$initialShadowRadius != 0 && width != 1)
            this.shadowRadius = vminus$initialShadowRadius * width;
    }

    @Redirect(method = "getRenderType(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;"
            )
    )
    private ResourceLocation redirectGetTextureLocation(LivingEntityRenderer instance, Entity entity) {
        IEntityVariantAccessor entityVariantAccessor = (IEntityVariantAccessor) entity;
        ResourceLocation variantTexture = entityVariantAccessor.vminus$getVariantTexture();
        if (variantTexture != null) {
            return variantTexture;
        }
        return instance.getTextureLocation(entity);
    }


}
