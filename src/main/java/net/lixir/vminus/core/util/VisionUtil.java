package net.lixir.vminus.core.util;

import net.lixir.vminus.core.conditions.VisionConditionArguments;
import net.lixir.vminus.core.visions.ItemVision;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class VisionUtil {
    public static boolean matchesIngredient(ItemStack targetStack, Ingredient.Value[] values) {
        for (Ingredient.Value value : values) {
            for (ItemStack stack : value.getItems()) {
                ItemStack replacementStack = ItemVision.getVision(stack).replace.value(new VisionConditionArguments(stack));
                Boolean banned = ItemVision.getVision(stack).ban.value(new VisionConditionArguments(stack));
                if (replacementStack != null && replacementStack.is(targetStack.getItem())) {
                    return true;
                }
                if ((banned == null || !banned) && stack.is(targetStack.getItem())) {
                    return true;
                }
            }
        }
        return false;
    }
}
