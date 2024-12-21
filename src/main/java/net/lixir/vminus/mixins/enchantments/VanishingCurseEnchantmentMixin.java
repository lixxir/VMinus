package net.lixir.vminus.mixins.enchantments;

import net.lixir.vminus.visions.util.EnchantmentVisionHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.VanishingCurseEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VanishingCurseEnchantment.class)
public abstract class VanishingCurseEnchantmentMixin {
    @Unique
    private final Enchantment enchantment = (Enchantment) (Object) this;

    @Inject(method = "isTreasureOnly", at = @At("RETURN"), cancellable = true)
    private void isTreasureOnly(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.isTreasure(enchantment, cir.getReturnValue() != null ? cir.getReturnValue() : false));
    }

    @Inject(method = "isCurse", at = @At("RETURN"), cancellable = true)
    private void isCurse(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.isCurse(enchantment, cir.getReturnValue() != null ? cir.getReturnValue() : false));
    }
}
