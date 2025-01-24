package net.lixir.vminus.vision.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.NumberUtil;
import net.lixir.vminus.vision.Vision;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class VisionValueHandler {

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

    public static boolean isBooleanMet(@Nullable JsonObject visionData, String property, @Nullable Object target) {
        return false;
    }

    public static boolean isBooleanMet(@Nullable JsonObject visionData, String property, @Nullable Object target, String containedProperty) {
        return false;
    }

    public static @Nullable String getFirstValidString(@Nullable JsonObject visionData, String checkFor) {
       return null;
    }

    public static @Nullable String getFirstValidString(@Nullable JsonObject visionData, String checkFor, @Nullable Object target) {
        return null;
    }

    public static @Nullable String getFirstValidString(@Nullable JsonObject visionData, String property, @Nullable Object target, @Nullable String containedProperty) {
        return null;
    }

    public static @Nullable Number getFirstValidNumber(@Nullable JsonObject visionData, String checkFor, @Nullable Object target, @Nullable String param) {
        return null;
    }

    public static @Nullable JsonObject getVisionData(@Nullable JsonObject visionData, @Nullable ItemStack itemstack, @Nullable Block block, @Nullable Entity entity) {
        return null;
    }

    public static @Nullable JsonObject getVisionData(@Nullable JsonObject visionData, @Nullable ItemStack itemstack, @Nullable Block block, @Nullable Entity entity, @Nullable EntityType<?> entityType) {
        return null;
    }



    public static Boolean checkValidParams(@Nullable JsonObject visionData, @Nullable String property) {
        return null;
    }

    public static boolean matchItemList(@Nullable JsonObject visionData, String checkFor, @Nullable ItemStack itemstack) {
        return false;
    }

    public static boolean EnchantmentCompatibleWith(@Nullable JsonObject visionData, Enchantment thisEnchantment, Enchantment otherEnchantment) {
        return false;
    }

    private static boolean matchesEnchantment(String value, Enchantment enchantment) {
        return false;
    }


    public static boolean checkConditions(JsonObject jsonObject, @Nullable Object target) {

        return true;
    }

    public static boolean checkConditions(String propertyName, @Nullable Object target) {

        return true;
    }

    public static ItemStack setNBTs(JsonObject itemData, ItemStack itemstack) {
        CompoundTag tag = itemstack.getOrCreateTag();
        if (itemData != null && itemData.has("nbt")) {
            JsonArray tagArray = itemData.getAsJsonArray("nbt");
            for (JsonElement element : tagArray) {
                JsonObject tagData = element.getAsJsonObject();
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

    public static List<String> getListOfStrings(JsonObject itemData, String checkFor, @Nullable Entity entity) {
        return getListOfStrings(itemData, checkFor, "value", entity);
    }

    public static List<String> getListOfStrings(JsonObject itemData, String checkFor, String valueString, @Nullable Entity entity) {
        JsonArray jsonArray = itemData.getAsJsonArray(checkFor);
        List<String> validValues = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            if (checkConditions(jsonObject, entity) && jsonObject.has(valueString)) {
                int weight = 1;
                if (jsonObject.has("weight"))
                    weight = jsonObject.get("weight").getAsInt();
                for (int i = 0; i < weight; i++)
                    validValues.add(jsonObject.get(valueString).getAsString());
            }
        }
        return validValues;
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

    public static boolean isBooleanMet(JsonObject visionData, String banned) {
        return  true;
    }

    public static int isNumberMet(JsonObject blockData, String lightLevel, int i, Block block) {
        return 0;
    }

    public static Float isNumberMet(JsonObject blockData, String friction, float v, Block vminus$block) {
        return Float.valueOf(0f);
    }

    public static float isNumberMet(JsonObject visionData, String volume, float defaultVolume, LivingEntity vminus$entity) {
        return 0f;
    }


    public static int isNumberMet(JsonObject itemData, String propertyMet, int i, ItemStack itemstack) {
        return 0;
    }

    public static int isNumberMet(JsonObject visionData, String minLevel, int defaultValue) {
        return 0;
    }
}
