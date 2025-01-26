package net.lixir.vminus.vision;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.VMinusConfig;
import net.lixir.vminus.vision.conditions.VisionConditionManager;
import net.lixir.vminus.vision.resources.VisionManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;

public class VisionProperties {


    public static @Nullable ItemStack getReplacementStack(@Nullable ItemStack itemStack) {
        if (itemStack == null)
            return null;
        String id = getString(Vision.getData(itemStack), "replace", itemStack);
        if (id != null && !id.isEmpty()) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
            if (item != null) {
                return new ItemStack(item);
            }
        }
        return null;
    }


    public static boolean isBanned(@Nullable ItemStack itemStack) {
        if (itemStack == null)
            return false;
        if (isItemConfigBanned(itemStack))
            return true;
        return getBoolean(Vision.getData(itemStack), "banned", itemStack);
    }

    public static boolean isBanned(@Nullable Entity entity) {
        if (entity == null)
            return false;
        return getBoolean(Vision.getData(entity), "banned", entity);
    }

    public static boolean isBanned(@Nullable MobEffect mobEffect) {
        if (mobEffect == null)
            return false;
        return getBoolean(Vision.getData(mobEffect), "banned", mobEffect);
    }


    public static boolean isBanned(@Nullable ItemStack itemStack, @Nullable JsonObject visionData) {
        if (itemStack == null)
            return false;
        if (isItemConfigBanned(itemStack))
            return true;
        return getBoolean(visionData, "banned", itemStack);
    }

    public static boolean isBanned(@Nullable Item item, @Nullable JsonObject visionData) {
        if (item == null)
            return false;
        if (isItemConfigBanned(item))
            return true;
        return getBoolean(visionData, "banned", item);
    }

    public static boolean isHidden(@Nullable ItemStack itemStack, @Nullable JsonObject visionData) {
        if (itemStack == null)
            return false;
        if (isItemConfigHidden(itemStack))
            return true;
        return getBoolean(visionData, "hidden", itemStack);
    }

    public static boolean isHidden(@Nullable Item item, @Nullable JsonObject visionData) {
        if (item == null)
            return false;
        if (isItemConfigHidden(item))
            return true;
        return getBoolean(visionData, "hidden", item);
    }

    public static boolean isUnalteredHidden(@Nullable Item item, @Nullable JsonObject visionData) {
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

    public static @Nullable String getString(String propertyName, @Nullable Object object) {
        return getString(null, null, null, propertyName, object, propertyName, null);
    }

    public static @Nullable String getString(JsonObject visionData, String propertyName, @Nullable Object object) {
        return getString(null, null, visionData, propertyName, object, propertyName, null);
    }

    public static @Nullable String getString(JsonObject searchObject, JsonObject visionData, String propertyName, @Nullable Object object) {
        return getString(null, searchObject, visionData, propertyName, object, propertyName, null);
    }

    public static @Nullable String getString(@Nullable String searchObjectName, JsonObject visionData, String propertyName, @Nullable Object object) {
        return getString(searchObjectName, null, visionData, propertyName, object, propertyName, null);
    }

    public static @Nullable String getString(JsonObject searchObject, JsonObject visionData, String propertyName, @Nullable Object object, String defaultValue) {
        return getString(null, searchObject, visionData, propertyName, object, propertyName, defaultValue);
    }

    public static @Nullable String getString(@Nullable String searchObjectName, @Nullable JsonObject searchObject, JsonObject visionData, String propertyName, @Nullable Object object, @Nullable String excludeCondition, String defaultValue) {
        JsonElement jsonElement = findPrimitiveElement(
                getSearchObject(searchObjectName, searchObject, visionData),
                visionData,
                propertyName,
                object,
                excludeCondition);
        if (jsonElement != null)
            return jsonElement.getAsString();
        return defaultValue;
    }

    public static boolean getBoolean(String propertyName, @Nullable Object object, boolean defaultValue) {
        return getBoolean(null, null, null, propertyName, object, propertyName, defaultValue);
    }

    public static boolean getBoolean(JsonObject visionData, String propertyName, @Nullable Object object) {
        return getBoolean(null, null, visionData, propertyName, object, propertyName, false);
    }

    public static boolean getBoolean(JsonObject visionData, String propertyName, @Nullable Object object, boolean defaultValue) {
        return getBoolean(null, null, visionData, propertyName, object, propertyName, defaultValue);
    }

    public static boolean getBoolean(JsonObject searchObject, JsonObject visionData, String propertyName, @Nullable Object object, boolean defaultValue) {
        return getBoolean(null, searchObject, visionData, propertyName, object, propertyName, defaultValue);
    }

    public static boolean getBoolean(@Nullable String searchObjectName, JsonObject visionData, String propertyName, @Nullable Object object) {
        return getBoolean(searchObjectName, null, visionData, propertyName, object, propertyName, false);
    }

    public static boolean getBoolean(@Nullable String searchObjectName, @Nullable JsonObject searchObject, JsonObject visionData, String propertyName, @Nullable Object object, @Nullable String excludeCondition, boolean defaultValue) {
        JsonElement jsonElement = findPrimitiveElement(
                getSearchObject(searchObjectName, searchObject, visionData),
                visionData,
                propertyName,
                object,
                excludeCondition);
        if (jsonElement != null)
            return jsonElement.getAsBoolean();
        return defaultValue;
    }

    public static Number getNumber(String propertyName, @Nullable Object object, Number defaultValue) {
        return getNumber(null, null, null, propertyName, object, propertyName, defaultValue);
    }

    public static Number getNumber(String propertyName, @Nullable Object object) {
        return getNumber(null, null, null, propertyName, object, propertyName, 0);
    }

    public static Number getNumber(JsonObject visionData, String propertyName, @Nullable Object object) {
        return getNumber(null, null, visionData, propertyName, object, propertyName, 0);
    }

    public static Number getNumber(JsonObject visionData, String propertyName, @Nullable Object object, Number defaultValue) {
        return getNumber(null, null, visionData, propertyName, object, propertyName, defaultValue);
    }

    public static Number getNumber(JsonObject searchObject, JsonObject visionData, String propertyName, @Nullable Object object, Number defaultValue) {
        return getNumber(null, searchObject, visionData, propertyName, object, propertyName, defaultValue);
    }

    public static Number getNumber(JsonObject searchObject, JsonObject visionData, String propertyName, @Nullable Object object) {
        return getNumber(null, searchObject, visionData, propertyName, object, propertyName, 0);
    }

    public static Number getNumber(@Nullable String searchObjectName, @Nullable JsonObject searchObject, JsonObject visionData, String propertyName, @Nullable Object object, @Nullable String excludeCondition, Number defaultValue) {
        JsonElement jsonElement = findPrimitiveElement(
                getSearchObject(searchObjectName, searchObject, visionData),
                visionData,
                propertyName,
                object,
                excludeCondition);
        if (jsonElement != null)
            return jsonElement.getAsNumber();
        return defaultValue;
    }

    public static JsonObject getSearchObject(@Nullable String searchObjectName, @Nullable JsonObject searchObject, JsonObject visionData) {
        return (searchObject == null ? findSearchObject(searchObjectName, visionData) : searchObject);
    }

    public static @Nullable JsonObject findSearchObject(String objectName, @Nullable Object object) {
        JsonObject visionData = Vision.getData(object);
        return findSearchObject(objectName, visionData, visionData, 0);
    }

    public static @Nullable JsonObject findSearchObject(String objectName, JsonObject visionData) {
        return findSearchObject(objectName, visionData, visionData, 0);
    }

    public static @Nullable JsonObject findSearchObject(String objectName, JsonObject visionData, int index) {
        return findSearchObject(objectName, visionData, visionData, index);
    }



    public static @Nullable JsonObject findSearchObject(String objectName, JsonObject containerObject, JsonObject visionData) {
        return findSearchObject(objectName, visionData, containerObject, 0);
    }

    // index is for skipping ones that were already checked / used in loops
    public static @Nullable JsonObject findSearchObject(String objectName, @Nullable JsonObject containerObject, JsonObject visionData, int index) {
        if (visionData == null)
            return null;
        if (objectName == null || objectName.isEmpty())
            return visionData;
        int count = 0;
        if (containerObject == null)
            containerObject = visionData;
        for (Map.Entry<String, JsonElement> entry : containerObject.entrySet()) {
            String key = entry.getKey();
            JsonElement jsonElement = entry.getValue();

            key = trimSign(key);

            if (key.equals(objectName)) {
                if (jsonElement.isJsonObject()) {
                    count++;
                    if (count > index) {
                        return jsonElement.getAsJsonObject();
                    }

                }
            }
        }

        return null;
    }


    public static @Nullable JsonArray findSearchArray(String objectName, JsonObject containerObject) {
        return findSearchArray(objectName, containerObject, 0);
    }

    // index is for skipping ones that were already checked / used in loops
    public static @Nullable JsonArray findSearchArray(String objectName, @Nullable JsonObject containerObject, int index) {
        if (objectName == null || objectName.isEmpty() || containerObject == null)
            return null;
        int count = 0;
        for (Map.Entry<String, JsonElement> entry : containerObject.entrySet()) {
            String key = entry.getKey();
            JsonElement jsonElement = entry.getValue();

            key = trimSign(key);

            if (key.equals(objectName)) {
                if (jsonElement.isJsonArray()) {
                    count++;
                    if (count > index) {
                        return jsonElement.getAsJsonArray();
                    }

                }
            }
        }

        return null;
    }

    public static String trimSign(String string) {
        String trimmed = string;
        if (trimmed.contains("$")) {
            trimmed = trimmed.substring(string.indexOf("$") + 1);
        }
        return trimmed;
    }

    public static @Nullable JsonElement findPrimitiveElement(@Nullable JsonObject searchObject, JsonObject visionData, String propertyName, @Nullable Object object, @Nullable String excludeCondition) {
        if (visionData == null)
            visionData = Vision.getData(object);
        if (visionData == null)
            return null;
        if (searchObject == null)
            searchObject = visionData;

        JsonElement foundElement = searchVisionData(searchObject, visionData, propertyName, object, excludeCondition);
        if (foundElement != null && foundElement.isJsonPrimitive()) {
            return foundElement;
        }
        return null;
    }

    // Finds the matching property in vision data excluding dollar sign names.
    public static @Nullable JsonElement searchVisionData(JsonObject searchObject, JsonObject visionData, String propertyName, @Nullable Object object, @Nullable String excludeCondition) {
        JsonElement matchElement = null;
        int priority = 500;
        for (Map.Entry<String, JsonElement> entry : searchObject.entrySet()) {
            String key = entry.getKey();
            final String originalKey = key;
            JsonElement jsonElement = entry.getValue();

            // strip variable name
            key = trimSign(key);

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


    public static FoodProperties getFoodProperties(JsonObject visionData, @Nullable Item item, @Nullable FoodProperties defaultProperties) {
        FoodProperties.Builder builder = new FoodProperties.Builder();

        JsonObject foodObject = findSearchObject(Names.FOOD_PROPERTIES, visionData);
        if (getBoolean(foodObject, visionData, Names.FAST_FOOD, item, defaultProperties != null && defaultProperties.isFastFood()))
            builder.fast();
        if (getBoolean(foodObject, visionData, Names.CAN_ALWAYS_EAT, item, defaultProperties != null && defaultProperties.canAlwaysEat()))
            builder.alwaysEat();
        if (getBoolean(foodObject, visionData, Names.IS_MEAT, item, defaultProperties != null && defaultProperties.isMeat()))
            builder.alwaysEat();

        builder.nutrition(getNumber(foodObject, visionData, Names.NUTRITION, item, defaultProperties != null ? defaultProperties.getNutrition() : 0).intValue());
        builder.saturationMod(getNumber(foodObject, visionData, Names.SATURATION, item, defaultProperties != null ? defaultProperties.getSaturationModifier() : 0).floatValue());


        JsonArray effectArray = findSearchArray(Names.FOOD_EFFECTS, foodObject);
        if (effectArray != null) {

            for (JsonElement jsonElement : effectArray.asList()) {
                JsonObject effectData = jsonElement.getAsJsonObject();
                if (!effectData.has(Names.ID))
                    continue;
                String effectId = effectData.get(Names.ID).getAsString();
                if (effectId == null || effectId.isEmpty())
                    continue;
                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectId));
                if (effect == null)
                    continue;
                int level = effectData.has(Names.LEVEL) ? effectData.get(Names.LEVEL).getAsInt() : 0;
                int duration = effectData.has(Names.DURATION) ? effectData.get(Names.DURATION).getAsInt() : 600;
                float chance = effectData.has(Names.CHANCE) ? effectData.get(Names.CHANCE).getAsFloat() : 1f;

                builder.effect(new MobEffectInstance(effect, duration, level), chance);
            }

        } else if (defaultProperties != null) {
            for (Pair<MobEffectInstance, Float> pair : defaultProperties.getEffects()) {
                builder.effect(pair.getFirst(), pair.getSecond());
            }
        }

        return builder.build();
    }

    public static class Names {
        // Items
        public final static String DAMAGEABLE = "damageable";
        public final static String GLINT = "glint";
        public final static String ENCHANTABLE = "enchantable";
        public final static String USE_DURATION = "use_duration";
        public final static String DURABILITY = "durability";
        public final static String NAMETAG_HIDDEN = "nametag_hidden";
        public final static String FIRE_RESISTANT = "fire_resistant";
        public final static String STACK_SIZE = "stack_size";
        public final static String ENCHANTABILITY = "enchantability";
        public final static String ATTRIBUTE = "attribute";
        public final static String SLOT = "slot";
        public final static String NAME = "name";
        public final static String REMOVE = "remove";
        public final static String VALUE = "value";

        public final static String BREAK_REPLACEMENT = "break_replacement";
        public final static String CARRY_NBT = "carry_nbt";

        public final static String CREATIVE_ORDER = "creative_order";
        public final static String TARGET = "target";
        public final static String TAB = "tab";
        public final static String BEFORE = "before";
        public final static String HIDDEN_TAB = "hidden_tab";

        // Food
        public final static String FOOD_PROPERTIES = "food_properties";
        public final static String FAST_FOOD = "eat_fast";
        public final static String CAN_ALWAYS_EAT = "always_edible";
        public final static String IS_MEAT = "meat";
        public final static String NUTRITION = "nutrition";
        public final static String SATURATION = "saturation";
        public final static String FOOD_EFFECTS = "food_effects";
        public final static String BURP_SOUND = "burp_sound";
        public final static String EAT_SOUND = "eat_sound";

        // Blocks
        public final static String DESTROY_TIME = "destroy_time";
        public final static String CONDUCTOR = "conductor";
        public final static String OCCLUDES = "occludes";
        public final static String EMISSIVE = "emissive";
        public final static String LIGHT_LEVEL = "light_level";
        public final static String VALID_SPAWN = "valid_spawn";
        public final static String SPEED_FACTOR = "speed_factor";
        public final static String JUMP_FACTOR = "jump_factor";
        public final static String FRICTION = "friction";
        public final static String BLAST_RESISTANCE = "blast_resistance";

        public final static String SOUND = "sound";
        public final static String STEP = "step";
        public final static String BREAK = "break";
        public final static String FALL = "fall";
        public final static String HIT = "hit";
        public final static String PLACE = "place";

        // Entities
        public final static String DAMPENS_VIBRATIONS = "dampens_vibrations";
        public final static String SILENT = "silent";
        public final static String WATER_SENSITIVE = "water_sensitive";
        public final static String UNDERWATER_BREATHING = "underwater_breathing";
        public final static String VOLUME = "volume";
        public final static String XP = "xp";

        // Enchantments
        public final static String PARTICLE = "particle";


        // Effects
        public final static String COLOR = "color";
        public final static String CATEGORY = "category";

        // Misc.
        public final static String ID = "id";
        public final static String LEVEL = "level";
        public final static String CHANCE = "chance";
        public final static String DURATION = "duration";
        public final static String REPLACE = "replace";
        public final static String BANNED = "banned";
        public final static String OPERATION = "operation";



    }

}
