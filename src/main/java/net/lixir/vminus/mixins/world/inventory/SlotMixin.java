package net.lixir.vminus.mixins.world.inventory;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.ItemReplacement;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
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
        ItemStack itemStack = getItem();
        Boolean ban = Vision.getValue(itemStack, VisionProperties.Items.BAN);
        if (Boolean.TRUE.equals(ban))
            cir.setReturnValue(false);
    }

    @Inject(method = "mayPlace", at = @At("RETURN"), cancellable = true)
    private void vMinus$preventBannedItemTake(@NotNull ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        Boolean ban = Vision.getValue(itemStack, VisionProperties.Items.BAN);
        if (Boolean.TRUE.equals(ban))
            cir.setReturnValue(false);
    }

    @Inject(method = "mayPickup", at = @At("RETURN"), cancellable = true)
    private void vMinus$preventBannedItemTake(Player player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = getItem();
        Boolean ban = Vision.getValue(itemStack, VisionProperties.Items.BAN);
        if (Boolean.TRUE.equals(ban))
            cir.setReturnValue(false);
    }

    @Inject(method = "onTake", at = @At("HEAD"), cancellable = true)
    private void vMinus$preventBannedItemTake(Player player, @NotNull ItemStack itemStack, CallbackInfo ci) {
        Boolean ban = Vision.getValue(itemStack, VisionProperties.Items.BAN);
        if (Boolean.TRUE.equals(ban)) {
            setChanged();
            ci.cancel();
        }
    }

    @Inject(method = "set", at = @At("HEAD"), cancellable = true)
    private void vminus$set(@NotNull ItemStack itemStack, CallbackInfo ci) {
        SlotAccessor slotAccessor = (SlotAccessor) vminus$slot;
        if (ItemReplacement.tryReplace(itemStack, replaced ->
                vminus$slot.container.setItem(slotAccessor.getSlot(), replaced))) {
            setChanged();
            ci.cancel();
        }
    }
}
