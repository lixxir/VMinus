package net.lixir.vminus.visions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.NumberUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisionValueHelper {
    private static boolean isModLoaded(String modId) {
        return net.minecraftforge.fml.ModList.get().isLoaded(modId);
    }

    public static double applyOperations(int initialValue, JsonArray valueArray, @Nullable ItemStack itemstack) {
        int modifiedValue = initialValue;
        for (JsonElement element : valueArray) {
            JsonObject durabilityData = element.getAsJsonObject();
            if (checkConditions(durabilityData, itemstack) && durabilityData.has("value")) {
                int value = durabilityData.get("value").getAsInt();
                String operation = "set";
                if (durabilityData.has("operation"))
                    operation = durabilityData.get("operation").getAsString() != null ? durabilityData.get("operation").getAsString() : "set";
                modifiedValue = (int) NumberUtil.modifyNumber(modifiedValue, value, operation);
            }
        }
        return modifiedValue;
    }

    /*
    TO DO:
        - Effect visions should also just ban the effects from being in food properties
     */
    public static FoodProperties getFoodProperties(JsonObject itemData, @Nullable ItemStack itemstack, @Nullable FoodProperties defaultProperties) {
        FoodProperties.Builder builder = new FoodProperties.Builder();
        if (itemData != null && itemData.has("food_properties")) {
            JsonArray foodPropertiesArray = itemData.getAsJsonArray("food_properties");
            for (JsonElement element : foodPropertiesArray) {
                JsonObject foodProperties = element.getAsJsonObject();
                if (checkConditions(foodProperties, itemstack)) {
                    if (foodProperties.has("nutrition")) {
                        builder.nutrition(foodProperties.get("nutrition").getAsInt());
                    } else if (defaultProperties != null) {
                        builder.nutrition(defaultProperties.getNutrition());
                    } else {
                        builder.nutrition(0);
                    }
                    if (foodProperties.has("saturation")) {
                        builder.saturationMod(foodProperties.get("saturation").getAsFloat());
                    } else if (defaultProperties != null) {
                        builder.saturationMod(defaultProperties.getSaturationModifier());
                    } else {
                        builder.saturationMod(0.0f);
                    }
                    if (foodProperties.has("is_meat")) {
                        if (foodProperties.get("is_meat").getAsBoolean())
                            builder.meat();
                    }
                    if (foodProperties.has("can_always_eat")) {
                        if (foodProperties.get("can_always_eat").getAsBoolean())
                            builder.alwaysEat();
                    }
                    if (foodProperties.has("fast_food")) {
                        if (foodProperties.get("fast_food").getAsBoolean())
                            builder.fast();
                    }
                    if (foodProperties.has("effects")) {
                        JsonArray effectsArray = foodProperties.getAsJsonArray("effects");
                        for (JsonElement effectElement : effectsArray) {
                            JsonObject effectData = effectElement.getAsJsonObject();
                            if (checkConditions(effectData, itemstack) && effectData.has("effect_id") && effectData.has("amplifier") && effectData.has("duration")) {
                                String effectId = effectData.get("effect_id").getAsString();
                                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectId));
                                if (effect != null) {
                                    int value = effectData.get("amplifier").getAsInt();
                                    int duration = effectData.get("duration").getAsInt();
                                    float chance = 1.0f;
                                    if (effectData.has("chance"))
                                        chance = effectData.get("chance").getAsFloat();
                                    builder.effect(new MobEffectInstance(effect, duration, value), chance);
                                }
                            }
                        }
                    }
                } else {
                    return defaultProperties;
                }
            }
        }
        return builder.build();
    }

    private static JsonObject getFirstMatchingElement(JsonObject visionData, String checkFor, ItemStack itemstack, Block block, Entity entity, String value) {
        visionData = getVisionData(visionData, itemstack, block, entity);
        if (checkValidParams(visionData, checkFor)) {
            JsonElement dataElement = visionData.get(checkFor);
            if (dataElement.isJsonArray()) {
                JsonArray dataArray = dataElement.getAsJsonArray();
                for (JsonElement element : dataArray) {
                    JsonObject dataObject = element.getAsJsonObject();
                    if (checkConditions(dataObject, itemstack, block, entity) && dataObject.has(value)) {
                        return dataObject;
                    }
                }
            } else if (dataElement.isJsonObject()) {
                JsonObject dataObject = dataElement.getAsJsonObject();
                if (checkConditions(dataObject, itemstack, block, entity) && dataObject.has(value)) {
                    return dataObject;
                }
            }
        }
        return null;
    }

    public static <T extends Number> T isNumberMet(@Nullable JsonObject visionData, String checkFor, T defaultValue) {
        return isNumberMet(visionData, checkFor, defaultValue, null, null, null);
    }

    public static <T extends Number> T isNumberMet(@Nullable JsonObject visionData, String checkFor, T defaultValue, Block block) {
        return isNumberMet(visionData, checkFor, defaultValue, null, block, null);
    }

    public static <T extends Number> T isNumberMet(@Nullable JsonObject visionData, String checkFor, T defaultValue, ItemStack itemstack) {
        return isNumberMet(visionData, checkFor, defaultValue, itemstack, null, null);
    }

    public static <T extends Number> T isNumberMet(@Nullable JsonObject visionData, String checkFor, T defaultValue, Entity entity) {
        return isNumberMet(visionData, checkFor, defaultValue, null, null, entity);
    }

    public static <T extends Number> T isNumberMet(@Nullable JsonObject visionData, String checkFor, T defaultValue, @Nullable ItemStack itemstack, @Nullable Block block, @Nullable Entity entity) {
        T result = defaultValue;
        visionData = getVisionData(visionData, itemstack, block, entity);
        if (checkValidParams(visionData, checkFor)) {
            JsonArray numberArray = visionData.getAsJsonArray(checkFor);
            for (JsonElement element : numberArray) {
                JsonObject numberData = element.getAsJsonObject();
                if (checkConditions(numberData, itemstack, block, entity) && numberData.has("value")) {
                    T value = convertToNumber(numberData.get("value").getAsString(), defaultValue);
                    String operation = numberData.has("operation") ? numberData.get("operation").getAsString() : "set";
                    result = modifyNumberAsType(result, value, operation);
                }
            }
        }
        return result;
    }

    private static <T extends Number> T modifyNumberAsType(T result, T value, String operation) {
        if (result instanceof Integer) {
            return (T) (Integer) (int) NumberUtil.modifyNumber(result.intValue(), value.intValue(), operation);
        } else if (result instanceof Double) {
            return (T) (Double) NumberUtil.modifyNumber(result.doubleValue(), value.doubleValue(), operation);
        } else if (result instanceof Long) {
            return (T) (Long) (long) NumberUtil.modifyNumber(result.longValue(), value.longValue(), operation);
        } else if (result instanceof Short) {
            return (T) (Short) (short) NumberUtil.modifyNumber(result.shortValue(), value.shortValue(), operation);
        } else if (result instanceof Float) {
            return (T) (Float) (float) NumberUtil.modifyNumber(result.floatValue(), value.floatValue(), operation);
        } else if (result instanceof Byte) {
            return (T) (Byte) (byte) NumberUtil.modifyNumber(result.byteValue(), value.byteValue(), operation);
        }
        throw new IllegalArgumentException("Unsupported number type: " + result.getClass());
    }

    private static <T extends Number> T convertToNumber(String value, T defaultValue) {
        if (defaultValue instanceof Integer) {
            return (T) Integer.valueOf(value);
        } else if (defaultValue instanceof Double) {
            return (T) Double.valueOf(value);
        } else if (defaultValue instanceof Long) {
            return (T) Long.valueOf(value);
        } else if (defaultValue instanceof Short) {
            return (T) Short.valueOf(value);
        } else if (defaultValue instanceof Float) {
            return (T) Float.valueOf(value);
        } else if (defaultValue instanceof Byte) {
            return (T) Byte.valueOf(value);
        }
        throw new IllegalArgumentException("Unsupported number type: " + defaultValue.getClass());
    }

    public static boolean isBooleanMet(@Nullable JsonObject itemData, String checkFor) {
        return isBooleanMet(itemData, checkFor, null, "value", null, null);
    }

    public static boolean isBooleanMet(@Nullable JsonObject itemData, String checkFor, ItemStack itemstack) {
        return isBooleanMet(itemData, checkFor, itemstack, "value", null, null);
    }

    public static boolean isBooleanMet(@Nullable JsonObject itemData, String checkFor, ItemStack itemstack, String param) {
        return isBooleanMet(itemData, checkFor, itemstack, param, null, null);
    }

    public static boolean isBooleanMet(@Nullable JsonObject itemData, String checkFor, Block block) {
        return isBooleanMet(itemData, checkFor, null, "value", block, null);
    }

    public static boolean isBooleanMet(@Nullable JsonObject itemData, String checkFor, Entity entity) {
        return isBooleanMet(itemData, checkFor, null, "value", null, entity);
    }

    public static boolean isBooleanMet(@Nullable JsonObject visionData, String checkFor, @Nullable ItemStack itemstack, @Nullable String param, @Nullable Block block, @Nullable Entity entity) {
        boolean booleanResult = false;
        visionData = getVisionData(visionData, itemstack, block, entity);
        if (checkValidParams(visionData, checkFor)) {
            JsonArray jsonData = visionData.getAsJsonArray(checkFor);
            for (JsonElement element : jsonData) {
                JsonObject conditionData = element.getAsJsonObject();
                if (checkConditions(conditionData, itemstack, block, entity) && conditionData.has(param)) {
                    boolean value = conditionData.get(param).getAsBoolean();
                    return value;
                }
            }
        }
        return booleanResult;
    }

    public static @Nullable JsonObject getVisionData(@Nullable JsonObject visionData, @Nullable ItemStack itemstack, @Nullable Block block, @Nullable Entity entity) {
        if (visionData != null)
            return visionData;
        if (itemstack != null)
            visionData = VisionHandler.getVisionData(itemstack);
        if (block != null)
            visionData = VisionHandler.getVisionData(block);
        if (entity != null)
            visionData = VisionHandler.getVisionData(entity);
        return visionData;
    }

    public static @Nullable String getFirstValidString(@Nullable JsonObject visionData, String checkFor, Entity entity, String param) {
        return getFirstValidString(visionData, checkFor, null, null, entity, param);
    }

    public static @Nullable String getFirstValidString(@Nullable JsonObject visionData, String checkFor, Block block, String param) {
        return getFirstValidString(visionData, checkFor, null, block, null, param);
    }

    public static @Nullable String getFirstValidString(@Nullable JsonObject visionData, String checkFor, ItemStack itemstack, String param) {
        return getFirstValidString(visionData, checkFor, itemstack, null, null, param);
    }

    public static @Nullable String getFirstValidString(@Nullable JsonObject visionData, String checkFor, Entity entity) {
        return getFirstValidString(visionData, checkFor, null, null, entity, "value");
    }

    public static @Nullable String getFirstValidString(@Nullable JsonObject visionData, String checkFor, Block block) {
        return getFirstValidString(visionData, checkFor, null, block, null, "value");
    }

    public static @Nullable String getFirstValidString(@Nullable JsonObject visionData, String checkFor, ItemStack itemstack) {
        return getFirstValidString(visionData, checkFor, itemstack, null, null, "value");
    }

    public static @Nullable String getFirstValidString(@Nullable JsonObject visionData, String checkFor) {
        return getFirstValidString(visionData, checkFor, null, null, null, "value");
    }

    public static @Nullable String getFirstValidString(@Nullable JsonObject visionData, String checkFor, @Nullable ItemStack itemstack, @Nullable Block block, @Nullable Entity entity, @Nullable String param) {
        visionData = getVisionData(visionData, itemstack, block, entity);
        if (checkValidParams(visionData, checkFor)) {
            JsonArray conditionArray = visionData.getAsJsonArray(checkFor);
            for (JsonElement element : conditionArray) {
                JsonObject elementObject = element.getAsJsonObject();
                if (checkConditions(elementObject, itemstack, block, entity) && elementObject.has(param)) {
                    return elementObject.get(param).getAsString();
                }
            }
        }
        return null;
    }

    public static boolean isStringMet(JsonObject itemData, String param, @Nullable ItemStack itemstack) {
        String stringResult = null;
        if (itemData == null)
            itemData = VisionHandler.getVisionData(itemstack);
        if (checkValidParams(itemData, param)) {
            JsonArray conditionArray = itemData.getAsJsonArray(param);
            for (JsonElement element : conditionArray) {
                JsonObject elementObject = element.getAsJsonObject();
                if (checkConditions(elementObject, itemstack) && elementObject.has("value")) {
                    stringResult = elementObject.get("value").getAsString();
                }
            }
        }
        if (stringResult == null) {
            return false;
        }
        //if (itemstack != null && itemstack.hasTag()) {
        //	CompoundTag nbt = itemstack.getTag();
        //	return nbt.contains(param, Tag.TAG_STRING) && nbt.getString(param).equals(stringResult);
        //}
        return false;
    }

    public static Map<String, Object> getBlockStateSetters(JsonObject blockData, String blockTag) {
        Map<String, Object> blockStateSetters = new HashMap<>();
        if (blockData.has(blockTag)) {
            JsonObject blockStates = blockData.getAsJsonObject(blockTag);
            if (blockStates.has("place_blockstate")) {
                JsonArray blockStateArray = blockStates.getAsJsonArray("place_blockstate");
                for (JsonElement element : blockStateArray) {
                    JsonObject stateObject = element.getAsJsonObject();
                    if (stateObject.has("name") && stateObject.has("value") && stateObject.has("type")) {
                        String name = stateObject.get("name").getAsString();
                        String type = stateObject.get("type").getAsString();
                        Object value;
                        switch (type) {
                            case "boolean":
                                value = stateObject.get("value").getAsBoolean();
                                break;
                            case "int":
                                value = stateObject.get("value").getAsInt();
                                break;
                            case "enum":
                                value = stateObject.get("value").getAsString();
                                break;
                            default:
                                throw new IllegalArgumentException("Unsupported block state type: " + type);
                        }
                        blockStateSetters.put(name, value);
                    }
                }
            }
        }
        return blockStateSetters;
    }

    public static Boolean checkValidParams(@Nullable JsonObject itemData, @Nullable String param) {
        if (itemData != null) {
            return itemData.has(param);
        }
        return false;
    }

    public static boolean matchItemList(@Nullable JsonObject visionData, String checkFor, @Nullable ItemStack itemstack) {
        if (visionData == null || itemstack == null || !visionData.has(checkFor)) {
            return false;
        }
        JsonArray itemArray = visionData.getAsJsonArray(checkFor);
        for (JsonElement element : itemArray) {
            String value = element.getAsString();
            if (value.startsWith("#")) {
                String tag = value.substring(1);
                if (itemstack.is(ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag)))) {
                    return true;
                }
            } else {
                if (ForgeRegistries.ITEMS.getKey(itemstack.getItem()).toString().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean EnchantmentCompatibleWith(@Nullable JsonObject visionData, Enchantment thisEnchantment, Enchantment otherEnchantment) {
        if (thisEnchantment == otherEnchantment) {
            return false;
        }
        if (visionData != null && visionData.has("compatible")) {
            JsonArray compatibleArray = visionData.getAsJsonArray("compatible");
            for (JsonElement element : compatibleArray) {
                String value = element.getAsString();
                if (matchesEnchantment(value, otherEnchantment)) {
                    return true;
                }
            }
            return false;
        }
        if (visionData != null && visionData.has("incompatible")) {
            JsonArray incompatibleArray = visionData.getAsJsonArray("incompatible");
            for (JsonElement element : incompatibleArray) {
                String value = element.getAsString();
                if (matchesEnchantment(value, otherEnchantment)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean matchesEnchantment(String value, Enchantment enchantment) {
        ResourceLocation enchantmentId = ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
        if (enchantmentId == null) {
            return false;
        }
        return enchantmentId.toString().equals(value);
    }

    public static boolean checkConditions(JsonObject conditionData, ItemStack itemstack) {
        return checkConditions(conditionData, itemstack, null, null);
    }

    public static boolean checkConditions(JsonObject conditionData, Block block) {
        return checkConditions(conditionData, null, block, null);
    }

    public static boolean checkConditions(JsonObject conditionData, Entity entity) {
        return checkConditions(conditionData, null, null, entity);
    }

    public static boolean checkConditions(JsonObject conditionData, @Nullable ItemStack itemstack, @Nullable Block block, @Nullable Entity entity) {
        boolean conditionsMet = true;
        if (conditionData.has("conditions")) {
            JsonElement conditionsElement = conditionData.get("conditions");
            if (conditionsElement.isJsonArray()) {
                JsonArray conditions = conditionsElement.getAsJsonArray();
                for (JsonElement conditionElement : conditions) {
                    JsonObject condition = conditionElement.getAsJsonObject();
                    conditionsMet = conditionsMet && evaluateConditions(condition, itemstack, block, entity);
                    if (!conditionsMet)
                        break;
                }
            } else {
                conditionsMet = false;
            }
        }
        return conditionsMet;
    }

    public static boolean checkInverted(JsonObject conditions) {
        if (conditions.has("inverted")) {
            return conditions.get("inverted").getAsBoolean();
        }
        return false;
    }

    public static boolean evaluateConditions(JsonObject conditions, @Nullable ItemStack itemstack, @Nullable Block block, @Nullable Entity entity) {
        if (conditions.has("mod_loaded")) {
            String modId = conditions.get("mod_loaded").getAsString();
            boolean isNegated = modId.startsWith("!");
            modId = modId.substring(isNegated ? 1 : 0);
            if (isNegated == isModLoaded(modId)) {
                return false;
            }
        }
        if (entity != null) {
            if (conditions.has("in_dimension")) {
                ResourceLocation dimensionLocation = entity.level().dimension().location();
                String dimensionId = conditions.get("in_dimension").getAsString();
                boolean isNegated = checkInverted(conditions);
                if (isNegated == dimensionLocation.equals(new ResourceLocation(dimensionId))) {
                    return false;
                }
            }
        }
        //if (block != null) {
        //
        //}
        if (itemstack != null) {
            if (conditions.has("is_item")) {
                String itemId = conditions.get("is_item").getAsString();
                boolean isNegated = checkInverted(conditions);
                if (isNegated == itemId.equals(ForgeRegistries.ITEMS.getKey(itemstack.getItem()).toString())) {
                    return false;
                }
            }
            if (conditions.has("is_damageable")) {
                boolean expectedValue = conditions.get("is_damageable").getAsBoolean();
                boolean actualValue = itemstack.getItem().canBeDepleted();
                if (actualValue != expectedValue) {
                    return false;
                }
            }
            if (conditions.has("is_damaged")) {
                boolean expectedValue = conditions.get("is_damaged").getAsBoolean();
                boolean actualValue = itemstack.isDamaged();
                if (actualValue != expectedValue) {
                    return false;
                }
            }
            if (conditions.has("is_enchanted")) {
                boolean expectedValue = conditions.get("is_enchanted").getAsBoolean();
                boolean actualValue = itemstack.isEnchanted();
                if (actualValue != expectedValue) {
                    return false;
                }
            }
            if (conditions.has("is_enchantable")) {
                boolean expectedValue = conditions.get("is_enchantable").getAsBoolean();
                boolean actualValue = itemstack.isEnchantable();
                if (actualValue != expectedValue) {
                    return false;
                }
            }
            if (conditions.has("is_edible")) {
                boolean expectedValue = conditions.get("is_edible").getAsBoolean();
                boolean actualValue = itemstack.isEdible();
                if (actualValue != expectedValue) {
                    return false;
                }
            }
            if (conditions.has("is_stackable")) {
                boolean expectedValue = conditions.get("is_stackable").getAsBoolean();
                boolean actualValue = itemstack.isStackable();
                if (actualValue != expectedValue) {
                    return false;
                }
            }
            if (conditions.has("is_rarity")) {
                String expectedRarity = conditions.get("is_rarity").getAsString().toUpperCase();
                boolean isNegated = checkInverted(conditions);
                Rarity actualRarity = itemstack.getItem().getRarity(itemstack);
                boolean rarityMatch = actualRarity.name().equals(expectedRarity);
                if (isNegated == rarityMatch) {
                    return false;
                }
            }
            if (conditions.has("is_enchantable")) {
                boolean expectedValue = conditions.get("is_enchantable").getAsBoolean();
                boolean actualValue = itemstack.getItem().isEnchantable(itemstack);
                if (actualValue != expectedValue) {
                    return false;
                }
            }
            if (conditions.has("is_tagged")) {
                String itemTag = conditions.get("is_tagged").getAsString();
                boolean isNegated = checkInverted(conditions);
                if (isNegated == itemstack.is(ItemTags.create(new ResourceLocation(itemTag)))) {
                    return false;
                }
            }
            if (conditions.has("has_nbt")) {
                String tagName = conditions.get("has_nbt").getAsString();
                boolean isNegated = checkInverted(conditions);
                if (itemstack.hasTag()) {
                    CompoundTag nbt = itemstack.getTag();
                    boolean tagExists = nbt.contains(tagName);
                    if (isNegated == tagExists) {
                        return false;
                    }
                } else if (!isNegated) {
                    return false;
                }
            }
            if (conditions.has("has_boolean_tag")) {
                String tagName = conditions.get("has_boolean_tag").getAsString();
                boolean isNegated = checkInverted(conditions);
                if (itemstack.hasTag()) {
                    CompoundTag nbt = itemstack.getTag();
                    boolean tagExists = nbt.contains(tagName, Tag.TAG_BYTE);
                    if (isNegated == tagExists) {
                        return false;
                    }
                } else if (!isNegated) {
                    return false;
                }
            }
            if (conditions.has("get_boolean_tag")) {
                JsonObject getBooleanTag = conditions.getAsJsonObject("get_boolean_tag");
                String tagName = getBooleanTag.get("name").getAsString();
                boolean expectedValue = getBooleanTag.get("value").getAsBoolean();
                boolean isNegated = checkInverted(conditions);
                if (isNegated) {
                    expectedValue = !expectedValue;
                }
                if (itemstack.hasTag()) {
                    CompoundTag nbt = itemstack.getTag();
                    if (nbt.contains(tagName, Tag.TAG_BYTE)) {
                        boolean nbtValue = nbt.getBoolean(tagName);
                        if (nbtValue != expectedValue) {
                            return false;
                        }
                    } else if (expectedValue) {
                        return false;
                    }
                } else if (expectedValue) {
                    return false;
                }
            }
            if (conditions.has("has_int_tag")) {
                String tagName = conditions.get("has_int_tag").getAsString();
                boolean isNegated = checkInverted(conditions);
                if (itemstack.hasTag()) {
                    CompoundTag nbt = itemstack.getTag();
                    boolean tagExists = nbt.contains(tagName, Tag.TAG_INT);
                    if (isNegated == tagExists) {
                        return false;
                    }
                } else if (!isNegated) {
                    return false;
                }
            }
            if (conditions.has("get_int_tag")) {
                JsonObject getIntTag = conditions.getAsJsonObject("get_int_tag");
                String tagName = getIntTag.get("name").getAsString();
                String rawExpectedValue = getIntTag.get("value").getAsString();
                boolean isNegated = checkInverted(conditions);
                int expectedValue = Integer.parseInt(rawExpectedValue);
                if (itemstack.hasTag()) {
                    CompoundTag nbt = itemstack.getTag();
                    if (nbt.contains(tagName, Tag.TAG_INT)) {
                        int nbtValue = nbt.getInt(tagName);
                        if ((nbtValue == expectedValue) == isNegated) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            if (conditions.has("has_double_tag")) {
                String tagName = conditions.get("has_double_tag").getAsString();
                boolean isNegated = checkInverted(conditions);
                if (itemstack.hasTag()) {
                    CompoundTag nbt = itemstack.getTag();
                    boolean tagExists = nbt.contains(tagName, Tag.TAG_DOUBLE);
                    if (isNegated == tagExists) {
                        return false;
                    }
                } else if (!isNegated) {
                    return false;
                }
            }
            if (conditions.has("get_double_tag")) {
                JsonObject getDoubleTag = conditions.getAsJsonObject("get_double_tag");
                String tagName = getDoubleTag.get("name").getAsString();
                String rawExpectedValue = getDoubleTag.get("value").getAsString();
                boolean isNegated = checkInverted(conditions);
                double expectedValue = Double.parseDouble(rawExpectedValue);
                if (itemstack.hasTag()) {
                    CompoundTag nbt = itemstack.getTag();
                    if (nbt.contains(tagName, Tag.TAG_DOUBLE)) {
                        double nbtValue = nbt.getDouble(tagName);
                        if ((nbtValue == expectedValue) == isNegated) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            if (conditions.has("has_long_tag")) {
                String tagName = conditions.get("has_long_tag").getAsString();
                boolean isNegated = checkInverted(conditions);
                if (itemstack.hasTag()) {
                    CompoundTag nbt = itemstack.getTag();
                    boolean tagExists = nbt.contains(tagName, Tag.TAG_LONG);
                    if (isNegated == tagExists) {
                        return false;
                    }
                } else if (!isNegated) {
                    return false;
                }
            }
            if (conditions.has("get_long_tag")) {
                JsonObject getLongTag = conditions.getAsJsonObject("get_long_tag");
                String tagName = getLongTag.get("name").getAsString();
                String rawExpectedValue = getLongTag.get("value").getAsString();
                boolean isNegated = checkInverted(conditions);
                long expectedValue = Long.parseLong(rawExpectedValue);
                if (itemstack.hasTag()) {
                    CompoundTag nbt = itemstack.getTag();
                    if (nbt.contains(tagName, Tag.TAG_LONG)) {
                        long nbtValue = nbt.getLong(tagName);
                        if ((nbtValue == expectedValue) == isNegated) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            if (conditions.has("has_short_tag")) {
                String tagName = conditions.get("has_short_tag").getAsString();
                boolean isNegated = checkInverted(conditions);
                if (itemstack.hasTag()) {
                    CompoundTag nbt = itemstack.getTag();
                    boolean tagExists = nbt.contains(tagName, Tag.TAG_SHORT);
                    if (isNegated == tagExists) {
                        return false;
                    }
                } else if (!isNegated) {
                    return false;
                }
            }
            if (conditions.has("get_short_tag")) {
                JsonObject getShortTag = conditions.getAsJsonObject("get_short_tag");
                String tagName = getShortTag.get("name").getAsString();
                String rawExpectedValue = getShortTag.get("value").getAsString();
                boolean isNegated = checkInverted(conditions);
                short expectedValue = Short.parseShort(rawExpectedValue);
                if (itemstack.hasTag()) {
                    CompoundTag nbt = itemstack.getTag();
                    if (nbt.contains(tagName, Tag.TAG_SHORT)) {
                        short nbtValue = nbt.getShort(tagName);
                        if ((nbtValue == expectedValue) == isNegated) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            if (conditions.has("has_string_tag")) {
                String tagName = conditions.get("has_string_tag").getAsString();
                boolean isNegated = checkInverted(conditions);
                if (itemstack.hasTag()) {
                    CompoundTag nbt = itemstack.getTag();
                    boolean tagExists = nbt.contains(tagName, Tag.TAG_STRING);
                    if (isNegated == tagExists) {
                        return false;
                    }
                } else if (!isNegated) {
                    return false;
                }
            }
            if (conditions.has("get_string_tag")) {
                JsonObject getStringTag = conditions.getAsJsonObject("get_string_tag");
                String tagName = getStringTag.get("name").getAsString();
                String rawExpectedValue = getStringTag.get("value").getAsString();
                boolean isNegated = checkInverted(conditions);
                String expectedValue = rawExpectedValue;
                if (itemstack.hasTag()) {
                    CompoundTag nbt = itemstack.getTag();
                    if (nbt.contains(tagName, Tag.TAG_STRING)) {
                        String nbtValue = nbt.getString(tagName);
                        return nbtValue.equals(expectedValue) != isNegated;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static ItemStack setNbts(JsonObject itemData, ItemStack itemstack) {
        CompoundTag tag = itemstack.getOrCreateTag();
        if (itemData != null && itemData.has("nbt")) {
            JsonArray tagArray = itemData.getAsJsonArray("nbt");
            for (JsonElement element : tagArray) {
                JsonObject tagData = element.getAsJsonObject();
                boolean conditionsMet = true;
                if (checkConditions(tagData, itemstack) && tagData.has("value") && tagData.has("type") && tagData.has("name")) {
                    String type = tagData.get("type").getAsString();
                    String name = tagData.get("name").getAsString();
                    JsonElement valueElement = tagData.get("value");
                    if (!tag.contains(name)) {
                        switch (type) {
                            case "boolean":
                            case "bool":
                                boolean booleanValue = valueElement.getAsBoolean();
                                tag.putBoolean(name, booleanValue);
                                break;
                            case "int":
                            case "integer":
                                int intValue = valueElement.getAsInt();
                                tag.putInt(name, intValue);
                                break;
                            case "decimal":
                            case "double":
                                double doubleValue = valueElement.getAsDouble();
                                tag.putDouble(name, doubleValue);
                                break;
                            case "string":
                                String stringValue = valueElement.getAsString();
                                tag.putString(name, stringValue);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        itemstack.setTag(tag);
        return itemstack;
    }

    public static List<String> getStringListFromJsonArray(JsonArray jsonArray) {
        List<String> values = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();
                if (jsonObject.has("value") && jsonObject.get("value").isJsonPrimitive()) {
                    values.add(jsonObject.get("value").getAsString());
                }
            }
        }
        return values;
    }

    public static Map<String, Double> getAttributes(JsonObject jsonData, @Nullable ItemStack itemstack) {
        Map<String, Double> attributes = new HashMap<>();
        if (jsonData.has("attributes")) {
            JsonArray attributeArray = jsonData.getAsJsonArray("attributes");
            for (JsonElement element : attributeArray) {
                JsonObject attributeData = element.getAsJsonObject();
                if (attributeData.has("attribute") && attributeData.has("value") && attributeData.has("operation")) {
                    if (checkConditions(attributeData, itemstack)) {
                        String attribute = attributeData.get("attribute").getAsString();
                        double value = attributeData.get("value").getAsDouble();
                        String operation = attributeData.get("operation").getAsString();
                        if (operation.equals("add")) {
                            attributes.put(attribute, attributes.getOrDefault(attribute, 0.0) + value);
                        } else if (operation.equals("multiply")) {
                            attributes.put(attribute, attributes.getOrDefault(attribute, 1.0) * value);
                        }
                    }
                }
            }
        }
        return attributes;
    }

    public static String getRarity(JsonObject itemData, ItemStack itemstack, String defaultRarity) {
        String rarity = defaultRarity;
        JsonArray rarityArray = itemData.getAsJsonArray("rarity");
        for (JsonElement element : rarityArray) {
            JsonObject rarityData = element.getAsJsonObject();
            if (checkConditions(rarityData, itemstack) && rarityData.has("value")) {
                rarity = rarityData.get("value").getAsString();
                break;
            }
        }
        return rarity;
    }

    public static List<String> getTooltips(JsonObject jsonData, @Nullable ItemStack itemstack, Boolean start) {
        List<String> tooltips = new ArrayList<>();
        if (jsonData.has("tooltip")) {
            JsonArray tooltipArray = jsonData.getAsJsonArray("tooltip");
            for (JsonElement element : tooltipArray) {
                JsonObject tooltipData = element.getAsJsonObject();
                if (checkConditions(tooltipData, itemstack) && tooltipData.has("value")) {
                    String tooltip = tooltipData.get("value").getAsString();
                    if (!tooltipData.has("position") || (tooltipData.get("position").getAsString().equals("start") && start) || (tooltipData.get("position").getAsString().equals("end") && !start)) {
                        tooltips.add(tooltip);
                    }
                }
            }
        }
        return tooltips;
    }
}
