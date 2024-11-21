package net.lixir.vminus.mixins.enchantments.functions;

import net.lixir.vminus.procedures.IsBannedEnchantmentProcedure;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(EnchantRandomlyFunction.class)
public abstract class EnchantRandomlyMixin {
    @Inject(method = "enchantItem", at = @At("HEAD"), cancellable = true)
    private static void enchantItem(ItemStack stack, Enchantment enchantment, RandomSource randomSource, CallbackInfoReturnable<ItemStack> info) {
        int i = Mth.nextInt(randomSource, enchantment.getMinLevel(), enchantment.getMaxLevel());
        String enchantmentKey = ForgeRegistries.ENCHANTMENTS.getKey(enchantment).toString();
        CompoundTag tag = stack.getOrCreateTag();
        int enchLimit = tag.contains("enchantment_limit") ? tag.getInt("enchantment_limit") : 999;
        double currentTotalEnchantmentLevel = 0.0;
        if (stack.isEnchanted()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                currentTotalEnchantmentLevel += entry.getValue();
            }
        }
        if (IsBannedEnchantmentProcedure.execute(enchantmentKey) || currentTotalEnchantmentLevel + i > enchLimit) {
            info.setReturnValue(stack);
        } else {
            stack.enchant(enchantment, i);
            info.setReturnValue(stack);
        }
    }
}
