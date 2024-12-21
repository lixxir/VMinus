package net.lixir.vminus.mixins.world;

import net.lixir.vminus.visions.util.ItemVisionHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public abstract class InventoryMixin {
    // Prevents items from getting added to the inventory if banned
    @Inject(method = "addResource(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void onAddResource(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (ItemVisionHelper.isBanned(stack)) cir.cancel();
    }
    @Inject(method = "addResource(ILnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void onAddResource(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (ItemVisionHelper.isBanned(stack)) cir.cancel();
    }
    @Inject(method = "getSlotWithRemainingSpace(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void onGetSlotWithRemainingSpace(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (ItemVisionHelper.isBanned(stack)) cir.cancel();
    }
    @Inject(method = "add(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void onAdd(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (ItemVisionHelper.isBanned(stack)) cir.cancel();
    }
    @Inject(method = "add(ILnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void onAdd(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (ItemVisionHelper.isBanned(stack)) cir.cancel();
    }
    @Inject(method = "placeItemBackInInventory(Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private void onPlaceItemBackInInventory(ItemStack stack, CallbackInfo ci) {
        if (ItemVisionHelper.isBanned(stack)) ci.cancel();
    }
    @Inject(method = "placeItemBackInInventory(Lnet/minecraft/world/item/ItemStack;Z)V", at = @At("HEAD"), cancellable = true)
    private void onPlaceItemBackInInventory(ItemStack stack, boolean drop, CallbackInfo ci) {
        if (ItemVisionHelper.isBanned(stack)) ci.cancel();
    }
}
