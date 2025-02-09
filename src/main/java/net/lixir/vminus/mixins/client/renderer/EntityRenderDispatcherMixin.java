package net.lixir.vminus.mixins.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    /*
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void preventRenderingEntities(Entity entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        float translucency = entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCY_KEY);
        if (translucency >= 1f)
            cir.setReturnValue(false);

    }

     */

    @Unique
    private static Entity vminus$currentShadowEntity;

    @Inject(method = "renderShadow", at = @At("HEAD"))
    private static void captureEntity(PoseStack poseStack, MultiBufferSource buffer, Entity entity, float shadowSize, float partialTicks, LevelReader level, float f, CallbackInfo ci) {
        vminus$currentShadowEntity = entity;
    }

    @ModifyArg(
            method = "renderShadow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;renderBlockShadow(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;DDDFF)V"
            ),
            index = 9
    )
    private static float modifyShadowF(float original) {
        float translucency = 0;
        if (vminus$currentShadowEntity != null) {
            translucency = vminus$currentShadowEntity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*1.1F;
        }
        return original - (original * translucency);
    }

    @Inject(method = "renderShadow", at = @At("RETURN"))
    private static void clearEntity(PoseStack poseStack, MultiBufferSource buffer, Entity entity, float shadowSize, float partialTicks, LevelReader level, float f, CallbackInfo ci) {
        vminus$currentShadowEntity = null;
    }
}