package net.lixir.vminus.vision.util;

import com.google.gson.JsonObject;
import net.lixir.vminus.VMinusConfig;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

public class VisionPropertyHandler {


    public static @Nullable ItemStack getDropReplacement(@Nullable ItemStack itemStack, @Nullable JsonObject visionData) {
        return null;
    }

    public static @Nullable ItemStack getIngredientReplacement(@Nullable ItemStack itemStack, @Nullable JsonObject visionData) {
        return null;
    }

    public static @Nullable ItemStack getReplacement(@Nullable ItemStack itemStack, @Nullable JsonObject visionData, @Nullable String specificReplace) {
        return null;
    }

    public static boolean matchesIngredient(ItemStack targetStack, Ingredient.Value[] values) {
        return false;
    }

    public static @Nullable ItemStack getRecipeOutputReplacement(@Nullable ItemStack itemStack) {
        return itemStack;
    }

}
