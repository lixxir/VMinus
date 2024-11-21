package net.lixir.vminus.mixins;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lixir.vminus.JsonValueUtil;
import net.lixir.vminus.VisionHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow
    @Final
    private PoseStack pose;
    @Shadow
    @Final
    private MultiBufferSource.BufferSource bufferSource;

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    public void renderItemDecorations(Font font, ItemStack itemstack, int x, int y, @Nullable String customText, CallbackInfo ci) {
        if (!itemstack.isEmpty()) {
            renderCustomTexture(itemstack, x, y);
        }
    }

    private void renderCustomTexture(ItemStack itemstack, int x, int y) {
        JsonObject visionData = VisionHandler.getVisionData(itemstack);
        this.pose.pushPose();
        ResourceLocation customTexture = getCustomTextureForItem(itemstack);
        if (customTexture != null) {
            RenderSystem.setShaderTexture(0, customTexture);
            if (JsonValueUtil.isBooleanMet(visionData, "front", itemstack)) {
                this.pose.translate(0.0F, 0.0F, 200.0F);
            } else {
                this.pose.translate(0.0F, 0.0F, 0.0F);
            }
            GuiGraphics guiGraphics = (GuiGraphics) (Object) this;
            guiGraphics.blit(customTexture, x, y, 0, 0, 16, 16, 16, 16);
        }
        this.pose.popPose();
    }

    private ResourceLocation getCustomTextureForItem(ItemStack itemstack) {
        JsonObject visionData = VisionHandler.getVisionData(itemstack);
        if (visionData != null && visionData.has("decorator")) {
            String decoString = JsonValueUtil.getFirstValidString(visionData, "decorator", itemstack);
            if (decoString != null && !decoString.isEmpty())
                return new ResourceLocation(decoString);
        }
        return null;
    }
}
