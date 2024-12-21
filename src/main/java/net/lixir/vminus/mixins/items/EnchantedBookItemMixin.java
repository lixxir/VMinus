package net.lixir.vminus.mixins.items;

import net.lixir.vminus.visions.util.EnchantmentVisionHelper;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemMixin {
    @Inject(method = "addEnchantment", at = @At("HEAD"), cancellable = true)
    private static void addEnchantment(ItemStack p_41154_, EnchantmentInstance enchantmentInstance, CallbackInfo ci) {
        Enchantment enchantment = enchantmentInstance.enchantment;
        if (EnchantmentVisionHelper.isBanned(enchantment)) {
            ci.cancel();
        }
    }

}