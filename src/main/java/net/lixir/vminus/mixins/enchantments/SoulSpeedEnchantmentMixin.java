package net.lixir.vminus.mixins.enchantments;

import net.lixir.vminus.visions.util.EnchantmentVisionHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.SoulSpeedEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoulSpeedEnchantment.class)
public abstract class SoulSpeedEnchantmentMixin {
    @Unique
    private final Enchantment enchantment = (Enchantment) (Object) this;

    @Inject(method = "getMaxLevel", at = @At("RETURN"), cancellable = true)
    private void getMaxLevel(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.getMaxLevel(enchantment, cir.getReturnValue() != null ? cir.getReturnValue() : 1));
    }

    @Inject(method = "isTreasureOnly", at = @At("RETURN"), cancellable = true)
    private void isTreasureOnly(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.isTreasure(enchantment, cir.getReturnValue() != null ? cir.getReturnValue() : false));
    }

    @Inject(method = "isTradeable", at = @At("RETURN"), cancellable = true)
    private void isTradeable(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.isTradeable(enchantment, cir.getReturnValue() != null ? cir.getReturnValue() : false));
    }

    @Inject(method = "isDiscoverable", at = @At("RETURN"), cancellable = true)
    private void isDiscoverable(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.isDiscoverable(enchantment, cir.getReturnValue() != null ? cir.getReturnValue() : false));
    }
}
