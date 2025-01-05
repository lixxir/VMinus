package net.lixir.vminus.mixins;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionValueHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mixin(value = CreativeModeTab.class, priority = 12000)
public abstract class CreativeTabMixin {
    @Unique
    private final CreativeModeTab vminus$creativeModeTab = (CreativeModeTab) (Object) this;

    @Inject(method = "fillItemList", at = @At("HEAD"), cancellable = true)
    private void vminus$fillItemList(NonNullList<ItemStack> itemList, CallbackInfo ci) {
        for (Item item : ForgeRegistries.ITEMS) {
            ItemStack itemStack = new ItemStack(item);
            JsonObject itemData = VisionHandler.getVisionData(itemStack);
            if (VisionValueHandler.isBooleanMet(itemData, "banned", itemStack)) {
                continue;
            }

            item.fillItemCategory(vminus$creativeModeTab, itemList);
        }
        ci.cancel();
    }
}
