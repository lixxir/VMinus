package net.lixir.vminus.mixins.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin {
    /*
    @Inject(method = "renderTabButton", at = @At("HEAD"), cancellable = true)
    protected void renderTabButton(GuiGraphics p_283590_, CreativeModeTab tab, CallbackInfo ci) {
        Boolean hide = CreativeTabVision.of(tab).hide.value();
        if (hide != null && hide)
            ci.cancel();
    }

     */

}
