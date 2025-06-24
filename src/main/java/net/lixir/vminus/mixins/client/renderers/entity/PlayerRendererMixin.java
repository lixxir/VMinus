package net.lixir.vminus.mixins.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lixir.vminus.sight.resource.SightManager;
import net.lixir.vminus.util.SizeAttributeUtil;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin<T extends LivingEntity> {
    @Inject(method = "scale(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;F)V", at = @At("HEAD"))
    private void vMinus$scale(T entity, PoseStack poseStack, float p_115316_, CallbackInfo ci) {
        if (!SightManager.get("size_attributes"))
            return;
        float width = SizeAttributeUtil.getWidth(entity);
        float height =  SizeAttributeUtil.getHeight(entity);
        if (height != 1 || width != 1)
            poseStack.scale(width, height, width);
    }
}
