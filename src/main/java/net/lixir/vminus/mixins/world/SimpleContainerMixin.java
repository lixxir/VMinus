package net.lixir.vminus.mixins.world;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleContainer.class)
public abstract class SimpleContainerMixin {
    @Inject(method = "addItem(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private void onAddItem(@NotNull ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir) {
        Boolean ban = VisionUtils.getOverrideValue(((VisionDuck) itemStack.getItem()), VisionProperties.Items.BAN, new VisionContext(itemStack));
        if (ban != null && ban)
            cir.setReturnValue(ItemStack.EMPTY);
    }

    @Inject(method = "canAddItem(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void onCanAddItem(@NotNull ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        VisionUtils.tryOverride(cir, ((VisionDuck) itemStack.getItem()), VisionProperties.Items.BAN, new VisionContext(itemStack));
    }

    @Inject(method = "setItem(ILnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private void onSetItem(int slot, @NotNull ItemStack itemStack, CallbackInfo ci) {
        VisionUtils.tryCancel(ci, ((VisionDuck) itemStack.getItem()), VisionProperties.Items.BAN, new VisionContext(itemStack));
    }
}

