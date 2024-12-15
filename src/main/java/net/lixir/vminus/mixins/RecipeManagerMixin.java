package net.lixir.vminus.mixins;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.visions.VisionValueHelper;
import net.lixir.vminus.visions.VisionHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    private static final Logger LOGGER = LogManager.getLogger();

    @Inject(at = @At("HEAD"), method = "apply", cancellable = true)
    private void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        Map<ResourceLocation, JsonElement> filteredMap = new HashMap<>();
        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            JsonElement jsonElement = entry.getValue();
            try {
                if (!jsonElement.isJsonObject()) {
                    LOGGER.warn("Skipping recipe {} as it is not a JsonObject", resourceLocation);
                    filteredMap.put(resourceLocation, jsonElement);
                    continue;
                }

                JsonObject jsonObject = jsonElement.getAsJsonObject();
                replaceRecipeIngredients(jsonObject);
                if (isRecipeBanned(jsonObject)) {
                    LOGGER.info("Recipe {} is banned and will be skipped", resourceLocation);
                    continue;
                }
                filteredMap.put(resourceLocation, jsonElement);
            } catch (IllegalArgumentException | JsonParseException e) {
                LOGGER.error("Parsing error loading recipe {}", resourceLocation, e);
                filteredMap.put(resourceLocation, jsonElement);
            }
        }
        map.clear();
        map.putAll(filteredMap);
    }

    private void replaceRecipeIngredients(JsonObject jsonObject) {
        try {
            if (jsonObject.has("ingredients")) {
                JsonElement ingredientsElement = jsonObject.get("ingredients");
                if (ingredientsElement.isJsonArray()) {
                    JsonArray ingredients = ingredientsElement.getAsJsonArray();
                    for (int i = 0; i < ingredients.size(); i++) {
                        JsonElement ingredientElement = ingredients.get(i);
                        processIngredientElement(ingredientElement);
                    }
                } else {
                    LOGGER.warn("Expected 'ingredients' to be a JsonArray but found {}", ingredientsElement.getClass().getSimpleName());
                }
            }
            if (jsonObject.has("key")) {
                JsonObject key = jsonObject.getAsJsonObject("key");
                for (Map.Entry<String, JsonElement> entry : key.entrySet()) {
                    processIngredientElement(entry.getValue());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error replacing recipe ingredients: {}", jsonObject, e);
        }
    }

    private void processIngredientElement(JsonElement ingredientElement) {
        try {
            if (ingredientElement.isJsonObject()) {
                replaceIngredient(ingredientElement.getAsJsonObject());
            } else if (ingredientElement.isJsonArray()) {
                JsonArray ingredientArray = ingredientElement.getAsJsonArray();
                for (JsonElement nestedElement : ingredientArray) {
                    processIngredientElement(nestedElement);
                }
            } else {
                LOGGER.warn("Unexpected ingredient format: {}", ingredientElement.getClass().getSimpleName());
            }
        } catch (Exception e) {
            LOGGER.error("Error processing ingredient element: {}", ingredientElement, e);
        }
    }

    private void replaceIngredient(JsonObject ingredient) {
        try {
            if (ingredient.has("item")) {
                String itemId = ingredient.get("item").getAsString();
                if (isValidResourceLocation(itemId)) {
                    ItemStack itemstack = createItemStack(itemId);
                    JsonObject itemData = VisionHandler.getVisionData(itemstack);
                    if (itemData != null) {
                        if (VisionValueHelper.isBooleanMet(itemData, "banned", itemstack) && !itemData.has("recipe_replace") && !itemData.has("replace")) {
                            ingredient.addProperty("item", "minecraft:air");
                        } else if (itemData.has("recipe_replace") || itemData.has("replace")) {
                            String replacementId = VisionValueHelper.getFirstValidString(itemData, "recipe_replace", itemstack);
                            if (!itemData.has("recipe_replace")) {
                                replacementId = VisionValueHelper.getFirstValidString(itemData, "replace", itemstack);
                            }
                            if (isValidResourceLocation(replacementId)) {
                                ingredient.addProperty("item", replacementId);
                            }
                        }
                    }
                } else {
                    LOGGER.warn("Invalid item ID for ingredient: {}", itemId);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error replacing ingredient: {}", ingredient, e);
        }
    }

    private boolean isRecipeBanned(JsonObject jsonObject) {
        try {
            return isAnyIngredientBanned(jsonObject) || isResultBanned(jsonObject);
        } catch (Exception e) {
            LOGGER.error("Error checking if recipe is banned: {}", jsonObject, e);
            return false;
        }
    }

    private boolean isAnyIngredientBanned(JsonObject jsonObject) {
        try {
            if (jsonObject.has("ingredients")) {
                JsonElement ingredientsElement = jsonObject.get("ingredients");
                if (ingredientsElement.isJsonArray()) {
                    JsonArray ingredients = ingredientsElement.getAsJsonArray();
                    for (JsonElement ingredientElement : ingredients) {
                        if (ingredientElement.isJsonObject() && isIngredientBanned(ingredientElement.getAsJsonObject())) {
                            return true;
                        }
                    }
                } else {
                    LOGGER.warn("Expected 'ingredients' to be a JsonArray but found {}", ingredientsElement.getClass().getSimpleName());
                }
            }
            if (jsonObject.has("key")) {
                JsonObject key = jsonObject.getAsJsonObject("key");
                for (Map.Entry<String, JsonElement> entry : key.entrySet()) {
                    if (entry.getValue().isJsonObject() && isIngredientBanned(entry.getValue().getAsJsonObject())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error checking if any ingredient is banned: {}", jsonObject, e);
        }
        return false;
    }

    private boolean isIngredientBanned(JsonObject ingredient) {
        try {
            if (ingredient.has("item")) {
                String itemId = ingredient.get("item").getAsString();
                JsonObject itemData = VisionHandler.getVisionData(createItemStack(itemId));
                return itemData != null && VisionValueHelper.isBooleanMet(itemData, "banned", createItemStack(itemId)) && !itemData.has("recipe_replace");
            }
        } catch (Exception e) {
            LOGGER.error("Error checking if ingredient is banned: {}", ingredient, e);
        }
        return false;
    }

    private boolean isValidResourceLocation(String resourceLocation) {
        try {
            new ResourceLocation(resourceLocation);
            return true;
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Invalid ResourceLocation: {}", resourceLocation);
            return false;
        }
    }

    private ItemStack createItemStack(String itemId) {
        try {
            if (isValidResourceLocation(itemId)) {
                return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)));
            }
        } catch (Exception e) {
            LOGGER.error("Error creating ItemStack for item ID: {}", itemId, e);
        }
        return ItemStack.EMPTY;
    }
}
