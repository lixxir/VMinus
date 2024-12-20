package net.lixir.vminus.helpers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class EnchantAndCurseHelper {
    public static int getTotalCurses(ItemStack itemstack) {
        int totalCurses = 0;
        if (!itemstack.isEmpty()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemstack);
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                Enchantment enchantment = entry.getKey();
                int level = entry.getValue();
                if (enchantment.isCurse()) {
                    int count = isModLoaded("detour") ? level : 1;
                    totalCurses += count;
                }
            }
        }
        return totalCurses;
    }

    public static int getTotalEnchantments(ItemStack itemstack) {
        int totalEnchantments = 0;
        if (!itemstack.isEmpty()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemstack);
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                Enchantment enchantment = entry.getKey();
                int level = entry.getValue();
                if (!enchantment.isCurse()) {
                    int count = isModLoaded("detour") ? level : 1;
                    totalEnchantments += count;
                }
            }
        }
        return totalEnchantments;
    }

    private static boolean isModLoaded(String modId) {
        return net.minecraftforge.fml.ModList.get().isLoaded(modId);
    }
}
