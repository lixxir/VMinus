package net.lixir.vminus.mixins.enchantments;

import net.lixir.vminus.visions.util.EnchantmentVisionHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageEnchantment.class)
public abstract class DamageEnchantmentMixin {
    @Unique
    private final Enchantment enchantment = (Enchantment) (Object) this;

    @Inject(method = "getMaxLevel", at = @At("RETURN"), cancellable = true)
    private void getMaxLevel(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.getMaxLevel(enchantment, cir.getReturnValue() != null ? cir.getReturnValue() : 1));
    }

    @Inject(method = "canEnchant", at = @At("RETURN"), cancellable = true)
    private void canEnchant(ItemStack itemstack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.canEnchant(enchantment, itemstack, cir.getReturnValue()));
    }
}
