package net.lixir.vminus.mixins.world;

import net.lixir.vminus.visions.util.ItemVisionHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleContainer.class)
public abstract class SimpleContainerMixin {
    @Inject(method = "addItem(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private void onAddItem(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (ItemVisionHelper.isBanned(stack)) cir.cancel();
    }
    @Inject(method = "canAddItem(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void onCanAddItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (ItemVisionHelper.isBanned(stack)) cir.cancel();
    }
    @Inject(method = "setItem(ILnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private void onSetItem(int slot, ItemStack stack, CallbackInfo ci) {
        if (ItemVisionHelper.isBanned(stack)) ci.cancel();
    }
}

