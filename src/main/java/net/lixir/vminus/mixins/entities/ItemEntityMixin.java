package net.lixir.vminus.mixins.entities;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHandler;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(at = @At("HEAD"), method = "fireImmune()Z", cancellable = true)
    private void fireImmune(CallbackInfoReturnable<Boolean> cir) {
        ItemEntity item = (ItemEntity) (Object) this;
        ItemStack itemstack = item.getItem();
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        if (itemData != null && itemData.has("fire_resistant")) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(itemData, "fire_resistant", itemstack));
        }
    }
}
