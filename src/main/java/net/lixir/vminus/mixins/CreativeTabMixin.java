package net.lixir.vminus.mixins;

import com.google.gson.JsonObject;
import net.lixir.vminus.VMinusConfig;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionPropertyHandler;
import net.lixir.vminus.visions.util.VisionValueHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
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
        for (ItemStack itemStack : displayItems) {
            JsonObject visionData = VisionHandler.getVisionData(itemStack);
            if (VisionPropertyHandler.isBanned(itemStack,visionData) ||  VisionPropertyHandler.isItemConfigHidden(itemStack)) {
                itemsToRemove.add(itemStack);
            }
        }
        displayItems.removeAll(itemsToRemove);
        itemsToRemove.clear();
        for (ItemStack itemStack : displayItemsSearchTab) {
            JsonObject visionData = VisionHandler.getVisionData(itemStack);
            if (VisionPropertyHandler.isBanned(itemStack,visionData) || VisionPropertyHandler.isItemConfigHidden(itemStack)) {
                itemsToRemove.add(itemStack);
            }
        }
        displayItemsSearchTab.removeAll(itemsToRemove);
    }

}
