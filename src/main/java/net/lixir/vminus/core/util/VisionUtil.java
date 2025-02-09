package net.lixir.vminus.core.util;

import com.google.gson.JsonObject;
import net.lixir.vminus.core.VisionProperties;
import net.lixir.vminus.core.Visions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class VisionUtil {
    public static boolean matchesIngredient(ItemStack targetStack, Ingredient.Value[] values) {
        for (Ingredient.Value value : values) {
            for (ItemStack stack : value.getItems()) {
                JsonObject visionData = Visions.getData(stack);
                ItemStack replacementStack = VisionProperties.getReplacementStack(stack);
                if (replacementStack != null && replacementStack.is(targetStack.getItem())) {
                    return true;
                }
                if (!VisionProperties.isBanned(stack, visionData) && stack.is(targetStack.getItem())) {
                    return true;
                }
            }
        }
        return false;
    }
}
