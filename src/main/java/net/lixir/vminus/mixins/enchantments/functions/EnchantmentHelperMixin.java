package net.lixir.vminus.mixins.enchantments.functions;

import com.google.common.collect.Lists;
import net.lixir.vminus.visions.util.EnchantmentVisionHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    /**
     * @author lixir
     * @reason To prevent banned enchantments from appearing in available enchantment results.
     */
    @Inject(method = "getAvailableEnchantmentResults", at = @At("RETURN"), cancellable = true)
    private static void getAvailableEnchantmentResults(int p_44818_, ItemStack stack, boolean p_44820_, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        List<EnchantmentInstance> list = Lists.newArrayList();
        boolean flag = stack.is(Items.BOOK);

        for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
            if ((!enchantment.isTreasureOnly() || p_44820_) && enchantment.isDiscoverable() && (enchantment.canApplyAtEnchantingTable(stack) || (flag && enchantment.isAllowedOnBooks()))) {
                for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                    if (p_44818_ >= enchantment.getMinCost(i) && p_44818_ <= enchantment.getMaxCost(i)) {
                        if (EnchantmentVisionHelper.isBanned(enchantment)) continue;
                        list.add(new EnchantmentInstance(enchantment, i));
                        break;
                    }
                }
            }
        }
        cir.setReturnValue(list);

    }

    /**
     * @author lixir
     * @reason To make getBlockEfficiency always return 0
     * in favor of adding attribute modifiers
     */
    @Overwrite
    public static int getBlockEfficiency(LivingEntity entity) {
        return 0;
    }
}
