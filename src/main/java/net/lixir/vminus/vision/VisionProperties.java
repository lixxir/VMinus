package net.lixir.vminus.vision;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.VMinusConfig;
import net.lixir.vminus.vision.conditions.VisionConditionManager;
import net.lixir.vminus.vision.resources.VisionManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;

public class VisionProperties {

    public static boolean isBanned(@Nullable ItemStack itemStack){
        if (itemStack == null)
            return false;
        if (isItemConfigBanned(itemStack))
            return true;
        return getBoolean(Vision.getData(itemStack), "banned", itemStack);
    }

    public static boolean isBanned(@Nullable ItemStack itemStack, @Nullable JsonObject visionData){
        if (itemStack == null)
            return false;
        if (isItemConfigBanned(itemStack))
            return true;
        return getBoolean(visionData, "banned", itemStack);
    }

    public static boolean isBanned(@Nullable Item item, @Nullable JsonObject visionData){
        if (item == null)
            return false;
        if (isItemConfigBanned(item))
            return true;
        return getBoolean(visionData, "banned", item);
    }

    public static boolean isHidden(@Nullable ItemStack itemStack, @Nullable JsonObject visionData){
        if (itemStack == null)
            return false;
        if (isItemConfigHidden(itemStack))
            return true;
        return getBoolean(visionData, "hidden", itemStack);
    }

    public static boolean isHidden(@Nullable Item item, @Nullable JsonObject visionData){
        if (item == null)
            return false;
        if (isItemConfigHidden(item))
            return true;
        return getBoolean(visionData, "hidden", item);
    }

    public static boolean isUnalteredHidden(@Nullable Item item, @Nullable JsonObject visionData){
        if (item == null)
            return false;
        return getBoolean(visionData, "unaltered_hidden", item);
    }

    public static boolean isHiddenInCreative(@Nullable ItemStack itemStack, @Nullable JsonObject visionData) {
        if (itemStack == null)
            return false;
        return isBanned(itemStack, visionData) || isHidden(itemStack, visionData) || isItemConfigHidden(itemStack);
    }

    public static boolean isHiddenInCreative(@Nullable Item item, @Nullable JsonObject visionData) {
        if (item == null)
            return false;
        return isBanned(item, visionData) || isHidden(item, visionData) || isItemConfigHidden(item);
    }

    public static boolean isItemConfigBanned(Item item) {
        ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(item);
        if (resourceLocation == null)
            return false;
        return VMinusConfig.BANNED_ITEMS.get().contains(resourceLocation.toString());
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

    public static boolean isItemConfigHidden(Item Item) {
        ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(Item);
        if (resourceLocation == null)
            return false;
        return VMinusConfig.HIDDEN_ITEMS.get().contains(resourceLocation.toString());
    }


    public static @Nullable String getString(JsonObject visionData, String propertyName, @Nullable Object object) {
        return getString(visionData, propertyName, object, propertyName);
    }

    public static @Nullable String getString(JsonObject visionData, String propertyName, @Nullable Object object, @Nullable String excludeCondition) {
        if (visionData == null)
            return null;

        JsonElement foundElement = searchVisionData(visionData, propertyName, object, excludeCondition);
        if (foundElement != null && foundElement.isJsonPrimitive()) {
            return foundElement.getAsString();
        }

        return null;
    }


    public static boolean getBoolean(JsonObject visionData, String propertyName, @Nullable Object object) {
        return getBoolean(visionData, propertyName, object, propertyName);
    }

    public static boolean getBoolean(JsonObject visionData, String propertyName, @Nullable Object object, @Nullable String excludeCondition) {
        if (visionData == null)
            return false;

        JsonElement foundElement = searchVisionData(visionData, propertyName, object, excludeCondition);
        if (foundElement != null && foundElement.isJsonPrimitive()) {
            return foundElement.getAsBoolean();
        }

        return false;
    }


    // Finds the matching property in vision data excluding dollar sign names.
    public static @Nullable JsonElement searchVisionData(JsonObject visionData, String propertyName, @Nullable Object object, @Nullable String excludeCondition) {
        JsonElement matchElement = null;
        int priority = 500;
        for (Map.Entry<String, JsonElement> entry : visionData.entrySet()) {
            String key = entry.getKey();
            final String originalKey = key;
            JsonElement jsonElement = entry.getValue();

            // strip variable name
            if (key.contains("$")) {
                key = key.substring(key.indexOf("$") + 1);
            }

            if (key.equals(propertyName)) {
                JsonArray conditionArray = null;
                if (visionData.has(originalKey + VisionManager.CONDITION_PATH)) {
                    JsonElement conditionElement = visionData.get(originalKey + VisionManager.CONDITION_PATH);
                    if (conditionElement.isJsonArray()) {
                        conditionArray = conditionElement.getAsJsonArray();

                    }
                }
                boolean conditionsMatch = VisionConditionManager.evaluateAllConditions(visionData, conditionArray, object, excludeCondition, originalKey);

                if (conditionsMatch) {
                    int matchPriority = 500;
                    if (visionData.has(originalKey + VisionManager.PRIORITY_PATH)) {

                        JsonElement conditionElement = visionData.get(originalKey + VisionManager.PRIORITY_PATH);
                        if (conditionElement.isJsonPrimitive()) {
                            matchPriority = conditionElement.getAsInt();
                            VMinus.LOGGER.debug("{} has a priority of {}", originalKey, matchPriority);
                        }
                    }
                    if (matchPriority >= priority) {
                        matchElement = jsonElement;
                        priority = matchPriority;
                    }
                }
            }
        }
        return matchElement;
    }
}
