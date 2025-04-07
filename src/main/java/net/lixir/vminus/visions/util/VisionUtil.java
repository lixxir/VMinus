package net.lixir.vminus.visions.util;

import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.ItemVision;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class VisionUtil {
    public static boolean matchesIngredient(ItemStack targetStack, Ingredient.Value[] values) {
        for (Ingredient.Value value : values) {
            for (ItemStack stack : value.getItems()) {
                if (stack == null || stack.isEmpty())
                    continue;
                VisionItemReplacement visionItemReplacement = ItemVision.of(stack).replace.value(new VisionConditionArguments(stack));
                ItemStack replacementStack = visionItemReplacement != null ? visionItemReplacement.itemStack() : ItemStack.EMPTY;
                Boolean banned = visionItemReplacement != null ? ItemVision.of(stack).ban.value(new VisionConditionArguments(stack)) : null;
                if (replacementStack != null && !replacementStack.isEmpty() && replacementStack.is(targetStack.getItem())) {
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
