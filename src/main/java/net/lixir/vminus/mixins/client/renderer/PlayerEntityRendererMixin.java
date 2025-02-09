package net.lixir.vminus.mixins.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerRenderer.class)
public class PlayerEntityRendererMixin {
    @Unique
    private static void detour$translucentHand(ModelPart modelPart, PoseStack matrices, int light, int overlay, MultiBufferSource vertexConsumers, AbstractClientPlayer player) {
        float translucency = Mth.clamp(player.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*1.5f, 0f, 1f);
        float colorDrain = Mth.clamp(translucency*2f, 0f, 1f);
        modelPart.render(matrices, vertexConsumers.getBuffer(RenderType.entityTranslucent(player.getSkinTextureLocation())), light, overlay, 1f- colorDrain, 1f- colorDrain, 1f - colorDrain, 1f - translucency);
    }

    @OnlyIn(Dist.CLIENT)
    @Redirect(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V", ordinal = 0))
    private void makeArmTranslucent(ModelPart modelPart, PoseStack matrices, VertexConsumer vertices, int light, int overlay, PoseStack matrices2, MultiBufferSource vertexConsumers, int light2, AbstractClientPlayer player) {
        detour$translucentHand(modelPart, matrices, light, overlay, vertexConsumers, player);
    }

    @OnlyIn(Dist.CLIENT)
    @Redirect(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V", ordinal = 1))
    private void makeSleeveTranslucent(ModelPart modelPart, PoseStack matrices, VertexConsumer vertices, int light, int overlay, PoseStack matrices2, MultiBufferSource vertexConsumers, int light2, AbstractClientPlayer player) {
        detour$translucentHand(modelPart, matrices, light, overlay, vertexConsumers, player);
    }
}