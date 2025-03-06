package net.lixir.vminus.mixins.client;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import net.lixir.vminus.core.Visions;
import net.lixir.vminus.core.VisionProperties;
import net.lixir.vminus.core.util.VisionItemDecorator;
import net.lixir.vminus.core.visions.ItemVision;
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
import java.util.List;

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

        AbstractContainerMenu menu = player.containerMenu;
        if (!menu.getCarried().isEmpty()) {
            return !itemstack.equals(menu.getCarried());
        }

        return true;
    }


    @Unique
    private void vminus$renderCustomTexture(ItemStack itemstack, int x, int y) {
        GuiGraphicsAccessor accessor = (GuiGraphicsAccessor) vminus$guiGraphics;
        ItemVision itemVision = ItemVision.getVision(itemstack);
        if (itemVision == null)
            return;
        if (itemstack.getTag() != null && itemstack.getTag().getBoolean("tab_item"))
            return;
        List<VisionItemDecorator> visionItemDecoratorList = itemVision.decorator.values();
        for (VisionItemDecorator visionItemDecorator : visionItemDecoratorList) {
            ResourceLocation texture = visionItemDecorator.texture();
            RenderSystem.setShaderTexture(0, texture);
            accessor.getPoseStack().translate(0.0F, 0.0F, visionItemDecorator.order());
            GuiGraphics guiGraphics = (GuiGraphics) (Object) this;
            guiGraphics.blit(texture, x, y, 0, 0, 16, 16, 16, 16);
            accessor.getPoseStack().popPose();
        }
    }
}
