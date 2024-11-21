package net.lixir.vminus.mixins;

import net.lixir.vminus.EnchantmentVisionHelper;
import net.minecraft.world.item.enchantment.ArrowKnockbackEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArrowKnockbackEnchantment.class)
public abstract class ArrowKnockbackEnchantmentMixin {
    @Unique
    private final Enchantment enchantment = (Enchantment) (Object) this;

    @Inject(method = "getMaxLevel", at = @At("HEAD"), cancellable = true)
    private void getMaxLevel(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.getMaxLevel(enchantment, cir.getReturnValue() != null ? cir.getReturnValue() : 1));
    }
}
