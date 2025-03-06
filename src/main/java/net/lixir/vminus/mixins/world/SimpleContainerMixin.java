package net.lixir.vminus.mixins.world;

import net.lixir.vminus.core.conditions.VisionConditionArguments;
import net.lixir.vminus.core.visions.ItemVision;
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
        Boolean banned = ItemVision.getVision(stack).ban.value(new VisionConditionArguments(stack));
        if (banned != null && banned) cir.setReturnValue(ItemStack.EMPTY);
    }
    @Inject(method = "canAddItem(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void onCanAddItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Boolean banned = ItemVision.getVision(stack).ban.value(new VisionConditionArguments(stack));
        if (banned != null && banned) cir.setReturnValue(false);
    }
    @Inject(method = "setItem(ILnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private void onSetItem(int slot, ItemStack stack, CallbackInfo ci) {
        Boolean banned = ItemVision.getVision(stack).ban.value(new VisionConditionArguments(stack));
        if (banned != null && banned) ci.cancel();
    }
}

