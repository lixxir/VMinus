package net.lixir.vminus.mixins;

import net.lixir.vminus.visions.ItemVision;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class SlotMixin {
    @Unique
    private final Slot vminus$slot = (Slot) (Object) this;

    @Inject(method = "hasItem", at = @At("RETURN"), cancellable = true)
    private void vminus$preventBannedItemTake(CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = vminus$slot.getItem();
        Boolean banned = ItemVision.of(itemStack).ban.value(new VisionConditionArguments(itemStack));
        if (banned != null && banned) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "mayPlace", at = @At("RETURN"), cancellable = true)
    private void vminus$preventBannedItemTake(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        Boolean banned = ItemVision.of(itemStack).ban.value(new VisionConditionArguments(itemStack));
        if (banned != null && banned) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "mayPickup", at = @At("RETURN"), cancellable = true)
    private void vminus$preventBannedItemTake(Player player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = vminus$slot.getItem();
        Boolean banned = ItemVision.of(itemStack).ban.value(new VisionConditionArguments(itemStack));
        if (banned != null && banned) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "onTake", at = @At("HEAD"), cancellable = true)
    private void vminus$preventBannedItemTake(Player player, ItemStack itemStack, CallbackInfo ci) {
        Boolean banned = ItemVision.of(itemStack).ban.value(new VisionConditionArguments(itemStack));
        if (banned != null && banned) {
            vminus$slot.setChanged();
            ci.cancel();
        }
    }

    @Inject(method = "set", at = @At("HEAD"), cancellable = true)
    private void vminus$set(ItemStack itemStack, CallbackInfo ci) {
        Boolean banned = ItemVision.of(itemStack).ban.value(new VisionConditionArguments(itemStack));
        if (banned != null && banned) {
            SlotAccessor slotAccessor = (SlotAccessor) vminus$slot;
            vminus$slot.container.setItem(slotAccessor.getSlot(), ItemStack.EMPTY);
            vminus$slot.setChanged();
            ci.cancel();
        }
    }
}
