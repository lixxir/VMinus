package net.lixir.vminus.mixins;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHandler;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mixin(value = CreativeModeTab.class, priority = 12000)
public abstract class CreativeTabMixin {
    @Shadow
    private Collection<ItemStack> displayItems;
    @Shadow
    private Set<ItemStack> displayItemsSearchTab;

    @Inject(method = "buildContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;rebuildSearchTree()V"))
    private void CreativeTabOverride(CreativeModeTab.ItemDisplayParameters displayContext, CallbackInfo ci) {
        List<ItemStack> itemsToRemove = new ArrayList<>();
        for (ItemStack itemstack : displayItems) {
            JsonObject itemData = VisionHandler.getVisionData(itemstack);
            if (VisionValueHandler.isBooleanMet(itemData, "banned", itemstack)) {
                itemsToRemove.add(itemstack);
            }
        }
        displayItems.removeAll(itemsToRemove);
        itemsToRemove.clear();
        for (ItemStack itemstack : displayItemsSearchTab) {
            JsonObject itemData = VisionHandler.getVisionData(itemstack);
            if (VisionValueHandler.isBooleanMet(itemData, "banned", itemstack)) {
                itemsToRemove.add(itemstack);
            }
        }
        displayItemsSearchTab.removeAll(itemsToRemove);
    }
}
