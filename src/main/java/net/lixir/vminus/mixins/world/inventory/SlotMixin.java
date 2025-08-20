package net.lixir.vminus.mixins.world.inventory;

import net.lixir.vminus.vision.util.ItemReplacement;
import net.lixir.vminus.vision.util.VisionUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {
    @Shadow public abstract ItemStack getItem();

    @Shadow public abstract void setChanged();

    @Unique
    private final Slot vminus$slot = (Slot) (Object) this;
    
    @Inject(method = "hasItem", at = @At("RETURN"), cancellable = true)
    private void vMinus$preventBannedItemTake(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = getItem();
        if (VisionUtils.isBanned(stack))
            cir.setReturnValue(false);
    }

    @Inject(method = "mayPlace", at = @At("RETURN"), cancellable = true)
    private void vMinus$preventBannedItemTake(@NotNull ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (VisionUtils.isBanned(stack))
            cir.setReturnValue(false);
    }

    @Inject(method = "mayPickup", at = @At("RETURN"), cancellable = true)
    private void vMinus$preventBannedItemTake(Player player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = getItem();
        if (VisionUtils.isBanned(stack))
            cir.setReturnValue(false);
    }

    @Inject(method = "onTake", at = @At("HEAD"), cancellable = true)
    private void vMinus$preventBannedItemTake(Player player, @NotNull ItemStack stack, CallbackInfo ci) {
        if (VisionUtils.isBanned(stack)) {
            setChanged();
            ci.cancel();
        }
    }

    @Inject(method = "set", at = @At("HEAD"), cancellable = true)
    private void vMinus$set(@NotNull ItemStack itemStack, CallbackInfo ci) {
        SlotAccessor slotAccessor = (SlotAccessor) vminus$slot;
        if (ItemReplacement.tryReplace(itemStack, replaced ->
                vminus$slot.container.setItem(slotAccessor.getSlot(), replaced))) {
            setChanged();
            ci.cancel();
        }
    }
}
