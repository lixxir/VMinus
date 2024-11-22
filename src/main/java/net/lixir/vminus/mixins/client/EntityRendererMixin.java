package net.lixir.vminus.mixins.client;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lixir.vminus.core.VisionValueHelper;
import net.lixir.vminus.core.VisionPropertyHelper;
import net.lixir.vminus.core.VisionHandler;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    @Inject(method = "renderNameTag", at = @At("HEAD"), cancellable = true)
    private void renderNameTag(T entity, Component name, PoseStack poseStack, MultiBufferSource bufferSource, int light, CallbackInfo ci) {
        if (entity instanceof Player player) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            JsonObject itemData = VisionHandler.getVisionData(helmet);
            String propertyMet = VisionPropertyHelper.propertyMet(itemData, "hides_nametag");
            if (!propertyMet.isEmpty()) {
                if (VisionValueHelper.isBooleanMet(itemData, propertyMet, helmet))
                    ci.cancel();
            }
        }
    }
}
