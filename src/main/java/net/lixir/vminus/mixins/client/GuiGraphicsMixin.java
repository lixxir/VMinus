package net.lixir.vminus.mixins.client;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow @Final private Minecraft minecraft;
    @Unique
    private final GuiGraphics vminus$guiGraphics = (GuiGraphics) (Object) this;

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    public void renderItemDecorations(Font font, ItemStack itemstack, int x, int y, @Nullable String customText, CallbackInfo ci) {
        if (!itemstack.isEmpty() && vminus$isItemInSlot(itemstack)) {
            vminus$renderCustomTexture(itemstack, x, y);
        }
    }
    @Unique
    private boolean vminus$isItemInSlot(ItemStack itemstack) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return false;
        }

        AbstractContainerMenu menu = player.inventoryMenu;
        if (!menu.getCarried().isEmpty()) {
            return !itemstack.equals(menu.getCarried());
        }

        return true;
    }


    @Unique
    private void vminus$renderCustomTexture(ItemStack itemstack, int x, int y) {
        GuiGraphicsAccessor accessor = (GuiGraphicsAccessor) vminus$guiGraphics;
        JsonObject visionData = Vision.getData(itemstack);
        accessor.getPoseStack().pushPose();
        ResourceLocation customTexture = vminus$getCustomTextureForItem(itemstack);
        if (customTexture != null) {
            RenderSystem.setShaderTexture(0, customTexture);
            if (VisionProperties.getBoolean(visionData, "front", itemstack)) {
                accessor.getPoseStack().translate(0.0F, 0.0F, 200.0F);
            } else {
                accessor.getPoseStack().translate(0.0F, 0.0F, 0.0F);
            }
            GuiGraphics guiGraphics = (GuiGraphics) (Object) this;
            guiGraphics.blit(customTexture, x, y, 0, 0, 16, 16, 16, 16);
        }
        accessor.getPoseStack().popPose();
    }

    @Unique
    private ResourceLocation vminus$getCustomTextureForItem(ItemStack itemstack) {
        JsonObject visionData = Vision.getData(itemstack);
        String decoString = VisionProperties.getString(visionData, "decorator", itemstack);
        if (itemstack.getTag() == null || !itemstack.getTag().getBoolean("tab_item")) {
            if (decoString != null && !decoString.isEmpty())
                return new ResourceLocation(decoString);
        }
        return null;
    }
}
