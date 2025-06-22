package net.lixir.vminus.mixins.world;

import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Inventory.class)
public abstract class InventoryMixin {

    // Prevents items from getting added to the inventory if banned

    /*
    @Inject(method = "addResource(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void onAddResource(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        Boolean banned = ItemVision.of(stack).ban.value(new VisionContext(stack));
        if (banned != null && banned) cir.setReturnValue(-1);
    }
    @Inject(method = "addResource(ILnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void onAddResource(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        Boolean banned = ItemVision.of(stack).ban.value(new VisionContext(stack));
        if (banned != null && banned) cir.setReturnValue(-1);
    }
    @Inject(method = "getSlotWithRemainingSpace(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void onGetSlotWithRemainingSpace(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        Boolean banned = ItemVision.of(stack).ban.value(new VisionContext(stack));
        if (banned != null && banned) cir.setReturnValue(-1);
    }
    @Inject(method = "add(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void onAdd(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Boolean banned = ItemVision.of(stack).ban.value(new VisionContext(stack));
        if (banned != null && banned) cir.setReturnValue(false);
    }
    @Inject(method = "add(ILnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void onAdd(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Boolean banned = ItemVision.of(stack).ban.value(new VisionContext(stack));
        if (banned != null && banned) cir.setReturnValue(false);
    }
    @Inject(method = "placeItemBackInInventory(Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private void onPlaceItemBackInInventory(ItemStack stack, CallbackInfo ci) {
        Boolean banned = ItemVision.of(stack).ban.value(new VisionContext(stack));
        if (banned != null && banned) ci.cancel();
    }
    @Inject(method = "placeItemBackInInventory(Lnet/minecraft/world/item/ItemStack;Z)V", at = @At("HEAD"), cancellable = true)
    private void onPlaceItemBackInInventory(ItemStack stack, boolean drop, CallbackInfo ci) {
        Boolean banned = ItemVision.of(stack).ban.value(new VisionContext(stack));
        if (banned != null && banned) ci.cancel();
    }

     */
}
