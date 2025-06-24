package net.lixir.vminus.mixins.world.entity;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionPropertyTypes;
import net.lixir.vminus.vision.util.VisionUtil;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public abstract class InventoryMixin {  // Prevents items from getting added to the inventory if ban.
    @Inject(method = "addResource(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void onAddResource(@NotNull ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        Boolean ban = VisionUtil.getOverrideValue(((VisionDuck) itemStack.getItem()), VisionPropertyTypes.Items.BAN, new VisionContext(itemStack));
        if (ban != null && ban)
            cir.setReturnValue(-1);
    }

    @Inject(method = "addResource(ILnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void onAddResource(int slot, @NotNull ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        Boolean ban = VisionUtil.getOverrideValue(((VisionDuck) itemStack.getItem()), VisionPropertyTypes.Items.BAN, new VisionContext(itemStack));
        if (ban != null && ban)
            cir.setReturnValue(-1);
    }

    @Inject(method = "getSlotWithRemainingSpace(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void onGetSlotWithRemainingSpace(@NotNull ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        Boolean ban = VisionUtil.getOverrideValue(((VisionDuck) itemStack.getItem()), VisionPropertyTypes.Items.BAN, new VisionContext(itemStack));
        if (ban != null && ban)
            cir.setReturnValue(-1);
    }

    @Inject(method = "add(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void onAdd(@NotNull ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        VisionUtil.tryOverride(cir, ((VisionDuck) itemStack.getItem()), VisionPropertyTypes.Items.BAN, new VisionContext(itemStack));
    }

    @Inject(method = "add(ILnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void onAdd(int slot, @NotNull ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        VisionUtil.tryOverride(cir, ((VisionDuck) itemStack.getItem()), VisionPropertyTypes.Items.BAN, new VisionContext(itemStack));
    }

    @Inject(method = "placeItemBackInInventory(Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private void onPlaceItemBackInInventory(@NotNull ItemStack itemStack, CallbackInfo ci) {
        VisionUtil.tryCancel(ci, ((VisionDuck) itemStack.getItem()), VisionPropertyTypes.Items.BAN, new VisionContext(itemStack));
    }

    @Inject(method = "placeItemBackInInventory(Lnet/minecraft/world/item/ItemStack;Z)V", at = @At("HEAD"), cancellable = true)
    private void onPlaceItemBackInInventory(@NotNull ItemStack itemStack, boolean drop, CallbackInfo ci) {
        VisionUtil.tryCancel(ci, ((VisionDuck) itemStack.getItem()), VisionPropertyTypes.Items.BAN, new VisionContext(itemStack));
    }
}
