package net.lixir.vminus.mixins;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.JsonValueUtil;
import net.lixir.vminus.VisionHandler;
import net.lixir.vminus.VminusMod;
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
                JsonObject jsonObject = GsonHelper.convertToJsonObject(jsonElement, "top element");
                replaceRecipeIngredients(jsonObject);
                if (isRecipeBanned(jsonObject)) {
                    continue;
                }
                filteredMap.put(resourceLocation, jsonElement);
            } catch (IllegalArgumentException | JsonParseException e) {
                VminusMod.LOGGER.error("Parsing error loading recipe {}", resourceLocation, e);
                filteredMap.put(resourceLocation, jsonElement);
            }
        }
        map.clear();
        map.putAll(filteredMap);
    }

    private void replaceRecipeIngredients(JsonObject jsonObject) {
        if (jsonObject.has("ingredients")) {
            JsonElement ingredientsElement = jsonObject.get("ingredients");
            if (ingredientsElement.isJsonArray()) {
                JsonArray ingredients = ingredientsElement.getAsJsonArray();
                for (int i = 0; i < ingredients.size(); i++) {
                    JsonElement ingredientElement = ingredients.get(i);
                    processIngredientElement(ingredientElement);
                }
            } else {
                VminusMod.LOGGER.warn("Expected 'ingredients' to be a JsonArray but found {}", ingredientsElement.getClass().getSimpleName());
            }
        }
        if (jsonObject.has("key")) {
            JsonObject key = jsonObject.getAsJsonObject("key");
            for (Map.Entry<String, JsonElement> entry : key.entrySet()) {
                JsonElement ingredientElement = entry.getValue();
                processIngredientElement(ingredientElement);
            }
        }
    }

    private void processIngredientElement(JsonElement ingredientElement) {
        if (ingredientElement.isJsonObject()) {
            JsonObject ingredient = ingredientElement.getAsJsonObject();
            replaceIngredient(ingredient);
        } else if (ingredientElement.isJsonArray()) {
            JsonArray ingredientArray = ingredientElement.getAsJsonArray();
            for (JsonElement nestedElement : ingredientArray) {
                processIngredientElement(nestedElement);
            }
        } else {
            VminusMod.LOGGER.warn("Unexpected ingredient format: {}", ingredientElement.getClass().getSimpleName());
        }
    }

    private void replaceIngredient(JsonObject ingredient) {
        if (ingredient.has("item")) {
            String itemId = ingredient.get("item").getAsString();
            ItemStack itemstack = createItemStack(itemId);
            JsonObject itemData = VisionHandler.getVisionData(itemstack);
            if (itemData != null) {
                if (JsonValueUtil.isBooleanMet(itemData, "banned", createItemStack(itemId)) && !itemData.has("recipe_replace") && !itemData.has("replace")) {
                    ingredient.addProperty("item", "minecraft:air");
                }
                if (itemData.has("recipe_replace") || itemData.has("replace")) {
                    String replacementId = JsonValueUtil.getFirstValidString(itemData, "recipe_replace", itemstack);
                    if (!itemData.has("recipe_replace")) {
                        replacementId = JsonValueUtil.getFirstValidString(itemData, "replace", itemstack);
                        if (isValidResourceLocation(replacementId)) {
                            ingredient.addProperty("item", replacementId);
                        }
                    }
                }
            }
        }
    }

    private boolean isRecipeBanned(JsonObject jsonObject) {
        return isAnyIngredientBanned(jsonObject) || isResultBanned(jsonObject);
    }

    private boolean isAnyIngredientBanned(JsonObject jsonObject) {
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
                VminusMod.LOGGER.warn("Expected 'ingredients' to be a JsonArray but found {}", ingredientsElement.getClass().getSimpleName());
            }
        }
        if (jsonObject.has("key")) {
            JsonObject key = jsonObject.getAsJsonObject("key");
            for (Map.Entry<String, JsonElement> entry : key.entrySet()) {
                JsonElement ingredientElement = entry.getValue();
                if (ingredientElement.isJsonObject() && isIngredientBanned(ingredientElement.getAsJsonObject())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isResultBanned(JsonObject jsonObject) {
        JsonElement resultElement = jsonObject.get("result");
        if (resultElement == null) {
            resultElement = jsonObject.get("output");
        }
        if (resultElement != null) {
            if (resultElement.isJsonPrimitive() && resultElement.getAsJsonPrimitive().isString()) {
                String itemId = resultElement.getAsString();
                return isItemBanned(itemId);
            } else if (resultElement.isJsonObject()) {
                String itemId = extractItemId(resultElement.getAsJsonObject());
                return isItemBanned(itemId);
            } else if (resultElement.isJsonArray()) {
                for (JsonElement element : resultElement.getAsJsonArray()) {
                    if (element.isJsonObject()) {
                        String itemId = extractItemId(element.getAsJsonObject());
                        if (isItemBanned(itemId)) {
                            return true;
                        }
                    } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                        String itemId = element.getAsString();
                        if (isItemBanned(itemId)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isItemBanned(String itemId) {
        JsonObject itemData = VisionHandler.getVisionData(createItemStack(itemId));
        return itemData != null && JsonValueUtil.isBooleanMet(itemData, "banned", createItemStack(itemId)) && !itemData.has("recipe_replace");
    }

    private boolean isModLoaded(String modId) {
        return net.minecraftforge.fml.ModList.get().isLoaded(modId);
    }

    private boolean isIngredientBanned(JsonObject ingredient) {
        if (ingredient.has("item")) {
            String itemId = ingredient.get("item").getAsString();
            JsonObject itemData = VisionHandler.getVisionData(createItemStack(itemId));
            return itemData != null && JsonValueUtil.isBooleanMet(itemData, "banned", createItemStack(itemId)) && !itemData.has("recipe_replace");
        }
        return false;
    }

    private String extractItemId(JsonObject itemObject) {
        if (itemObject.has("item")) {
            return itemObject.get("item").getAsString();
        } else if (itemObject.has("id")) {
            return itemObject.get("id").getAsString();
        }
        return null;
    }

    private boolean isValidResourceLocation(String resourceLocation) {
        try {
            new ResourceLocation(resourceLocation);
            return true;
        } catch (IllegalArgumentException e) {
            VminusMod.LOGGER.warn("Invalid ResourceLocation: {}", resourceLocation);
            return false;
        }
    }

    private ItemStack createItemStack(String itemId) {
        if (itemId != null && !itemId.contains("#")) {
            if (isValidResourceLocation(itemId)) {
                return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)));
            }
        }
        return ItemStack.EMPTY;
    }
}
