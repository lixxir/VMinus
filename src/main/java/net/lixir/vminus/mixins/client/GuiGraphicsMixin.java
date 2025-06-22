package net.lixir.vminus.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow
    @Final
    private PoseStack pose;
    @Unique
    private final GuiGraphics vminus$guiGraphics = (GuiGraphics) (Object) this;
    @Shadow
    @Final
    private Minecraft minecraft;

    /*
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
    private void vminus$renderCustomTexture(ItemStack itemStack, int x, int y) {
        ItemVision itemVision = ItemVision.of(itemStack);
        if (itemStack.getTag() != null && itemStack.getTag().getBoolean("tab_item"))
            return;
        List<VisionItemDecorator> visionItemDecoratorList = itemVision.decorator.values(new VisionContext(itemStack));
        this.pose.pushPose();
        for (VisionItemDecorator visionItemDecorator : visionItemDecoratorList) {
            ResourceLocation texture = visionItemDecorator.texture();
            RenderSystem.setShaderTexture(0, texture);
            this.pose.translate(0.0F, 0.0F, visionItemDecorator.order());
            vminus$guiGraphics.blit(texture, x, y, 0, 0, 16, 16, 16, 16);
        }
        this.pose.popPose();
    }

     */
}
