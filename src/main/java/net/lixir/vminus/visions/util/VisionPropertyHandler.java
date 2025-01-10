package net.lixir.vminus.visions.util;

import com.google.gson.JsonObject;
import net.lixir.vminus.VMinusConfig;
import net.lixir.vminus.visions.VisionHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public class VisionPropertyHandler {

    public static boolean isBanned(@Nullable ItemStack itemStack, @Nullable JsonObject visionData){
        if (itemStack == null)
            return false;
        if (isItemConfigBanned(itemStack))
            return true;
        return getBoolean(itemStack, "banned", visionData);
    }
    public static boolean isBanned(@Nullable ItemStack itemStack){
        if (itemStack == null)
            return false;
        if (isItemConfigBanned(itemStack))
            return true;
        return getBoolean(itemStack, "banned", null);
    }

    public static boolean isItemConfigBanned(ItemStack itemStack) {
        ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
        if (resourceLocation == null)
            return false;
        return VMinusConfig.BANNED_ITEMS.get().contains(resourceLocation.toString());
    }

    public static boolean isItemConfigHidden(ItemStack itemStack) {
        ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
        if (resourceLocation == null)
            return false;
        return VMinusConfig.HIDDEN_ITEMS.get().contains(resourceLocation.toString());
    }

    public static @Nullable ItemStack getReplacement(@Nullable ItemStack itemStack) {
        return getReplacement(itemStack, null, null);
    }
    public static @Nullable ItemStack getReplacement(@Nullable ItemStack itemStack, String specificReplace) {
        return getReplacement(itemStack, null, specificReplace);
    }

    public static @Nullable ItemStack getDropReplacement(@Nullable ItemStack itemStack, @Nullable JsonObject visionData) {
        return getReplacement(itemStack, visionData, "replace_drop");
    }

    public static @Nullable ItemStack getIngredientReplacement(@Nullable ItemStack itemStack, @Nullable JsonObject visionData) {
        return getReplacement(itemStack, visionData, "replace_recipe_ingredient");
    }

    public static @Nullable ItemStack getReplacement(@Nullable ItemStack itemStack, @Nullable JsonObject visionData, @Nullable String specificReplace) {
        if (visionData == null) {
            visionData = VisionHandler.getVisionData(itemStack);
        }
        if (visionData == null)
            return null;

        String replaceID = VisionValueHandler.getFirstValidString(visionData, "replace", itemStack);
        if ((replaceID == null || replaceID.isEmpty())
                && (specificReplace != null && !specificReplace.isEmpty())
                && visionData.has(specificReplace))
            replaceID = VisionValueHandler.getFirstValidString(visionData, specificReplace, itemStack);

        return replaceID != null && !replaceID.isEmpty()
                ? new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(replaceID))))
                : null;
    }

    public static boolean matchesIngredient(ItemStack targetStack, Ingredient.Value[] values) {
        for (Ingredient.Value value : values) {
            for (ItemStack stack : value.getItems()) {
                JsonObject visionData = VisionHandler.getVisionData(stack);
                ItemStack replacementStack = VisionPropertyHandler.getIngredientReplacement(stack, visionData);

                if (replacementStack != null && replacementStack.is(targetStack.getItem())) {
                    return true;
                }

                if (!VisionPropertyHandler.isBanned(stack, visionData) && stack.is(targetStack.getItem())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static @Nullable ItemStack getRecipeOutputReplacement(@Nullable ItemStack itemStack) {
        JsonObject visionData = VisionHandler.getVisionData(itemStack);
        if (visionData != null) {
            boolean isBanned = VisionPropertyHandler.isBanned(itemStack, visionData);
            if (isBanned)
                return new ItemStack(Items.AIR);

            ItemStack replacementStack = VisionPropertyHandler.getReplacement(itemStack, visionData, "replace_recipe_output");

            if (replacementStack != null && !replacementStack.isEmpty())
                return replacementStack;
        }
        return itemStack;
    }

    public static boolean getBoolean(@Nullable ItemStack itemstack, String property){
       return getBoolean(itemstack, property, null);
    }

    public static boolean getBoolean(@Nullable ItemStack itemStack, String property, @Nullable JsonObject visionData){
        if (visionData == null)
            visionData = VisionHandler.getVisionData(itemStack);
        if (visionData != null && visionData.has(property)
                && VisionValueHandler.isBooleanMet(visionData, property, itemStack)) {
            return true;
        }
        return false;
    }

    public static boolean getBoolean(@Nullable Block block, String property){
        return getBoolean(block, property, null);
    }

    public static boolean getBoolean(@Nullable Block block, String property, @Nullable JsonObject visionData){
        if (visionData == null)
            visionData = VisionHandler.getVisionData(block);
        if (visionData != null && visionData.has(property)
                && VisionValueHandler.isBooleanMet(visionData, property, block)) {
            return true;
        }
        return false;
    }
}
