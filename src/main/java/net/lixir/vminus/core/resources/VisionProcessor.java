package net.lixir.vminus.core.resources;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.core.VisionProperties;
import net.lixir.vminus.core.VisionProperty;
import net.lixir.vminus.core.VisionType;
import net.lixir.vminus.core.conditions.VisionConditions;
import net.lixir.vminus.core.util.VisionAttribute;
import net.lixir.vminus.core.util.VisionFoodProperties;
import net.lixir.vminus.core.values.BasicVisionValue;
import net.lixir.vminus.registry.VMinusRarities;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class VisionProcessor {
    public static JsonObject processJson(String folderName, JsonElement jsonFile) throws JsonParseException {
        VisionType visionType = VisionType.getFromDirectory(folderName);
        JsonObject jsonFileObject = jsonFile.getAsJsonObject();
        String listType = visionType.getListName();
        String singleName = visionType.getSingleName();

        VMinus.LOGGER.debug("Testing process: {} ", jsonFile);

        // Adds single keys to the key list
        keyToArray(jsonFileObject, singleName, listType);
        keyToArray(jsonFileObject, "tag", listType);

        JsonObject processedJsonObject = new JsonObject();

        if (jsonFileObject.has(listType) && jsonFileObject.get(listType).isJsonArray()) {
            processedJsonObject.add(listType, jsonFileObject.get(listType));
        }
        JsonArray variables = jsonFileObject.has("variables") ? jsonFileObject.getAsJsonArray("variables") : new JsonArray();
        JsonArray globalConditions = jsonFileObject.has("conditions") ? jsonFileObject.getAsJsonArray("conditions") : new JsonArray();

        for (Map.Entry<String, JsonElement> entry : jsonFileObject.entrySet()) {
            String key = entry.getKey();
            JsonElement jsonElement = entry.getValue();
            if (key.endsWith("/conditions"))
                continue;

            JsonArray mergedConditions = new JsonArray();
            if (jsonFileObject.has(key + "/conditions")) {
                mergedConditions.addAll(jsonFileObject.getAsJsonArray(key + "/conditions"));
            }
            mergedConditions.addAll(globalConditions);

            if (jsonElement.isJsonPrimitive() && !key.endsWith("/priority")) {
                JsonObject wrappedObject = new JsonObject();

                wrappedObject.add("value", jsonElement);
                wrappedObject.add("conditions", mergedConditions);

                JsonArray jsonArray = new JsonArray();
                jsonArray.add(wrappedObject);
                processedJsonObject.add(key, jsonArray);
            } else if (jsonElement.isJsonArray() && !key.equals(listType) && !key.equals("conditions") && !key.equals("variables")) {
                JsonArray newArray = new JsonArray();
                for (JsonElement arrayElement : jsonElement.getAsJsonArray()) {
                    if (arrayElement.isJsonPrimitive()) {
                        JsonObject wrappedObject = new JsonObject();
                        wrappedObject.add("value", arrayElement);
                        wrappedObject.add("conditions", mergedConditions);
                        newArray.add(wrappedObject);
                    } else if (jsonElement.isJsonObject()) {
                        JsonObject newObject = jsonElement.getAsJsonObject();
                        for (Map.Entry<String, JsonElement> objectEntry : newObject.entrySet()) {
                            String entryKey = objectEntry.getKey();
                            JsonElement entryElement = objectEntry.getValue();

                            newObject.add(entryKey, entryElement);
                        }
                        newObject.add("conditions", mergedConditions);
                        newArray.add(newObject);
                    } else {
                        newArray.add(arrayElement);
                    }
                }
                processedJsonObject.add(key, newArray);
            } else if (jsonElement.isJsonObject()) {
                JsonArray newArray = wrapObjectInArray(jsonElement, mergedConditions);

                processedJsonObject.add(key, newArray);
            } else {
                processedJsonObject.add(key, jsonElement);
            }
        }
        JsonArray listArray = processedJsonObject.getAsJsonArray(listType);
        for (JsonElement listElement : listArray) {
            if (!listElement.isJsonPrimitive())
                throw new JsonParseException(listType + " contains a value that is not JsonPrimitive");
            JsonPrimitive jsonPrimitive = listElement.getAsJsonPrimitive();
            String listKey = jsonPrimitive.getAsString();
            if (!isValidListKey(listKey)) {
                throw new JsonParseException("Invalid list key: '" + listKey + "'. Allowed characters: [a-z, 0-9, :, !, #, *, /, _]");
            }
        }

        VMinus.LOGGER.info("Processed JSON: {}", processedJsonObject);

        return processedJsonObject;
    }

    private static @NotNull JsonArray wrapObjectInArray(JsonElement jsonElement, JsonArray mergedConditions) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray newArray = new JsonArray();
        JsonObject newObject = new JsonObject();
        for (Map.Entry<String, JsonElement> objectEntry : jsonObject.entrySet()) {
            String entryKey = objectEntry.getKey();
            JsonElement entryElement = objectEntry.getValue();

            newObject.add(entryKey, entryElement);
        }
        newObject.add("conditions", mergedConditions);
        newArray.add(newObject);
        return newArray;
    }

    public static void keyToArray(JsonObject jsonFileObject, String singleName, String listType) {
        if (!jsonFileObject.has(singleName))
            return;

        JsonElement singleElement = jsonFileObject.get(singleName);

        if (!singleElement.isJsonPrimitive())
            return;

        String singleValue = singleElement.getAsString();
        if (singleName.equals("tag")) {
            if (!singleValue.startsWith("#")) {
                singleValue = "#" + singleValue;
            }
        }

        JsonArray jsonArray;

        if (jsonFileObject.has(listType) && jsonFileObject.get(listType).isJsonArray()) {
            jsonArray = jsonFileObject.get(listType).getAsJsonArray();
        } else {
            jsonArray = new JsonArray();
            jsonFileObject.add(listType, jsonArray);
        }

        jsonFileObject.remove(singleName);
        jsonArray.add(singleValue);
    }

    public static void parseInt(JsonObject jsonObject, String key, VisionProperty<BasicVisionValue<Integer>, Integer> property, int min, int max) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            int value = arrayObject.getAsJsonPrimitive("value").getAsInt();
            if (value > max || value < min) {
                throw new JsonParseException(value + " is not an accepted integer value for " + key + ". Must be within " + min + " to " + max);
            }
            addBasicVisionValue(property, value, arrayObject, jsonObject, key);
        }
    }

    public static void parseFloat(JsonObject jsonObject, String key, VisionProperty<BasicVisionValue<Float>, Float> property, float min, float max) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            float value = arrayObject.getAsJsonPrimitive("value").getAsFloat();
            if (value > max || value < min) {
                throw new JsonParseException(value + " is not an accepted float value for " + key + ". Must be within " + min + " to " + max);
            }
            addBasicVisionValue(property, value, arrayObject, jsonObject, key);
        }
    }


    public static void parseVisionAttribute(JsonObject jsonObject, String key, VisionProperty<BasicVisionValue<VisionAttribute>, VisionAttribute> property) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            double value;
            if (arrayObject.has("value")) {
                if (!arrayObject.get("value").isJsonPrimitive()) {
                    throw new JsonParseException(arrayObject.get("value") + " is not a valid double.");
                }
                value = arrayObject.get("value").getAsJsonPrimitive().getAsDouble();
            } else {
                value = 0;
            }
            Boolean replace = arrayObject.has("replace") && arrayObject.get("replace").isJsonPrimitive() && arrayObject.getAsJsonPrimitive("replace").getAsBoolean();
            Boolean remove = arrayObject.has("remove")  && arrayObject.get("remove").isJsonPrimitive() && arrayObject.getAsJsonPrimitive("remove").getAsBoolean();

            String uuidString = arrayObject.has("uuid") ? arrayObject.getAsJsonPrimitive("uuid").getAsString() : null;
            UUID uuid;
            if (uuidString == null) {
                uuid = UUID.randomUUID();
            } else {
                try {
                    uuid = UUID.fromString(uuidString);
                }catch (IllegalArgumentException e) {
                    throw new JsonParseException(uuidString + " is not a valid UUID.");
                }
            }

            String id = arrayObject.getAsJsonPrimitive("id").getAsString();
            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(id));
            if (attribute == null)
                throw new JsonParseException(id + " is not a valid attribute.");

            String operationString = arrayObject.has("operation") ? arrayObject.getAsJsonPrimitive("operation").getAsString() : "addition";
            AttributeModifier.Operation operation;
            if (operationString != null) {
                try {
                    operation = AttributeModifier.Operation.valueOf(operationString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new JsonParseException(operationString + " is not a valid operation.");
                }
            } else {
                operation = AttributeModifier.Operation.ADDITION;
            }

            String slotString = arrayObject.has("slot") ? arrayObject.getAsJsonPrimitive("slot").getAsString() : null;
            EquipmentSlot equipmentSlot = null;
            if (slotString != null) {
                try {
                    equipmentSlot = EquipmentSlot.valueOf(slotString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new JsonParseException(slotString + " is not a valid equipment slot.");
                }
            }

            String name = arrayObject.has("name") ? arrayObject.getAsJsonPrimitive("name").getAsString() : null;
            if (name == null) {
                if (id.contains(".")) {
                    name = id.substring(id.indexOf(".") + 1);
                } else if (id.contains(":")) {
                    name = id.substring(id.indexOf(":") + 1);
                }
                if (name != null)
                    name = name.replaceAll("_", " ");
            }

            AttributeModifier attributeModifier = new AttributeModifier(uuid, name, value, operation);
            VisionAttribute visionAttribute = new VisionAttribute(remove,replace, attributeModifier, attribute, equipmentSlot, id);

            addBasicVisionValue(property, visionAttribute, arrayObject, jsonObject, key);
        }
    }

    public static void parseItemStack(JsonObject jsonObject, String key, VisionProperty<BasicVisionValue<ItemStack>, ItemStack> property) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            String value = arrayObject.getAsJsonPrimitive("value").getAsString();
            ResourceLocation resourceLocation;
            try {
                resourceLocation = new ResourceLocation(value);
            } catch (Exception e ) {
                throw new JsonParseException(value + " is not a valid ResourceLocation.");
            }
            Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
            if (item == null)
                throw new JsonParseException(resourceLocation + " is not a valid item.");
            ItemStack itemStack = item.getDefaultInstance();

            addBasicVisionValue(property, itemStack, arrayObject, jsonObject, key);
        }
    }


    public static void parseBoolean(JsonObject jsonObject, String key, VisionProperty<BasicVisionValue<Boolean>, Boolean> property) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            Boolean value = arrayObject.getAsJsonPrimitive("value").getAsBoolean();

            addBasicVisionValue(property, value, arrayObject, jsonObject, key);
        }
    }

    public static void parseSoundType(JsonObject jsonObject, String key, VisionProperty<BasicVisionValue<SoundType>, SoundType> property) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            SoundEvent fallSound = parseSoundInObject(arrayObject, "fall");
            if (fallSound == null)
                fallSound = SoundEvents.STONE_FALL;
            SoundEvent stepSound = parseSoundInObject(arrayObject, "step");
            if (stepSound == null)
                stepSound = SoundEvents.STONE_STEP;
            SoundEvent breakSound = parseSoundInObject(arrayObject, "break");
            if (breakSound == null)
                breakSound = SoundEvents.STONE_BREAK;
            SoundEvent placeSound = parseSoundInObject(arrayObject, "place");
            if (placeSound == null)
                placeSound = SoundEvents.STONE_PLACE;
            SoundEvent hitSound = parseSoundInObject(arrayObject, "hit");
            if (hitSound == null)
                hitSound = SoundEvents.STONE_HIT;

            float pitch = arrayObject.has("pitch") ? arrayObject.getAsJsonPrimitive("pitch").getAsFloat() : 1f;
            if (pitch > 0) {
                throw new JsonParseException(pitch + " is not an accepted pitch value for " + key + ". Must be within be greater than 0");
            }

            float level = arrayObject.has("level") ? arrayObject.getAsJsonPrimitive("level").getAsFloat() : 1f;
            if (level > 0) {
                throw new JsonParseException(level + " is not an accepted level value for " + key + ". Must be within be greater than 0");
            }

            SoundType soundType = new SoundType(level, pitch, breakSound, stepSound, placeSound, hitSound, fallSound);
            addBasicVisionValue(property, soundType, arrayObject, jsonObject, key);
        }
    }


    public static void parseVisionFoodProperties(JsonObject jsonObject, String key, VisionProperty<BasicVisionValue<VisionFoodProperties>, VisionFoodProperties> property) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            Integer nutrition = null;
            if (arrayObject.has("nutrition")) {
                nutrition = arrayObject.getAsJsonPrimitive("nutrition").getAsInt();
                if (nutrition > 20 || nutrition < 0) {
                    throw new JsonParseException(nutrition + " is not an accepted nutrition for " + key + ". Must be within 0 to 20");
                }
            }
            Float saturation = null;
            if (arrayObject.has("saturation")) {
                saturation = arrayObject.getAsJsonPrimitive("saturation").getAsFloat();
                if (saturation < 0) {
                    throw new JsonParseException(saturation + " is not an accepted saturation for " + key + ". Must be greater than 0");
                }
            }
            Boolean alwaysEdible = null;
            if (arrayObject.has("always_edible"))
                alwaysEdible = arrayObject.getAsJsonPrimitive("always_edible").getAsBoolean();
            Boolean isMeat = null;
            if (arrayObject.has("is_meat"))
                isMeat = arrayObject.getAsJsonPrimitive("is_meat").getAsBoolean();

            SoundEvent eatSound = parseSoundInObject(arrayObject, "eat_sound");
            SoundEvent burpSound = parseSoundInObject(arrayObject, "burp_sound");

            List<Pair<MobEffectInstance, Float>> effects = parseFoodEffects(arrayObject);

            VisionFoodProperties visionFoodProperties = new VisionFoodProperties(nutrition, saturation, alwaysEdible, isMeat, eatSound, burpSound, effects);

            addBasicVisionValue(property, visionFoodProperties, arrayObject, jsonObject, key);
        }
    }

    private static List<Pair<MobEffectInstance, Float>> parseFoodEffects(JsonObject jsonObject) throws JsonParseException {
        List<Pair<MobEffectInstance, Float>> effects = new ArrayList<>();

        if (jsonObject.has("effects")) {
            JsonArray effectsArray = jsonObject.getAsJsonArray("effects");
            for (JsonElement effectElement : effectsArray) {
                JsonObject effectObject = effectElement.getAsJsonObject();

                String effectId = effectObject.getAsJsonPrimitive("effect_id").getAsString();
                ResourceLocation effectLocation;
                try {
                    effectLocation = new ResourceLocation(effectId);
                } catch (Exception e) {
                    throw new JsonParseException(effectId + " is an invalid effect ID");
                }

                MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(effectLocation);
                if (mobEffect == null) {
                    throw new JsonParseException(effectId + " is not a registered effect");
                }

                int amplifier = effectObject.has("amplifier") ? effectObject.getAsJsonPrimitive("amplifier").getAsInt() : 0;
                int duration = effectObject.has("duration") ? effectObject.getAsJsonPrimitive("duration").getAsInt() : 200;
                float chance = effectObject.has("chance") ? effectObject.getAsJsonPrimitive("chance").getAsFloat() : 1.0f;

                if (amplifier < 0) {
                    throw new JsonParseException("Amplifier cannot be negative for effect: " + effectId);
                }
                if (duration <= 0) {
                    throw new JsonParseException("Duration must be greater than 0 for effect: " + effectId);
                }
                if (chance < 0 || chance > 1) {
                    throw new JsonParseException("Chance must be between 0 and 1 for effect: " + effectId);
                }

                effects.add(new Pair<>(new MobEffectInstance(mobEffect, duration, amplifier), chance));
            }
        }

        return effects;
    }


    private static SoundEvent parseSoundInObject(JsonObject jsonObject, String key) throws JsonParseException {
        if (jsonObject.has(key)) {
            String soundString = jsonObject.getAsJsonPrimitive(key).getAsString();
            ResourceLocation soundLocation;
            try {
                soundLocation = new ResourceLocation(soundString);
            } catch (Exception e) {
                throw new JsonParseException(soundString + " is an invalid sound location for " + key);
            }
            SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(soundLocation);
            if (soundEvent != null) {
                return soundEvent;
            } else {
                throw new JsonParseException(soundString + " as " + key + " does not exist in the sound registries");
            }
        }
        return null;
    }

    public static void parseRarity(JsonObject jsonObject, String key, VisionProperty<BasicVisionValue<Rarity>, Rarity> property) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            String rarityString = arrayObject.getAsJsonPrimitive("value").getAsString();
            Rarity value;
            switch (rarityString.toUpperCase()) {
                case "LEGENDARY" -> value = VMinusRarities.LEGENDARY;
                case "INVERTED" -> value = VMinusRarities.INVERTED;
                case "UNOBTAINABLE" -> value = VMinusRarities.UNOBTAINABLE;
                case "DELICACY" -> value = VMinusRarities.DELICACY;
                default -> {
                    try {
                        value = Rarity.valueOf(rarityString.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new JsonParseException(rarityString + " is not a valid rarity for " + key);
                    }
                }
            }

            addBasicVisionValue(property, value, arrayObject, jsonObject, key);
        }
    }

    public static <E extends Enum<E>> void parseEnum(
            JsonObject jsonObject,
            String key,
            VisionProperty<BasicVisionValue<E>, E> property,
            Class<E> enumClass) throws JsonParseException {

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            String valueString = arrayObject.getAsJsonPrimitive("value").getAsString();
            E value;

            try {
                value = Enum.valueOf(enumClass, valueString.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new JsonParseException(valueString + " is not a valid value for " + key);
            }

            addBasicVisionValue(property, value, arrayObject, jsonObject, key);
        }
    }

    public static ArrayList<String> getEntries(JsonObject jsonObject, VisionType visionType) throws JsonParseException {
        JsonArray listArray;
        String listName = visionType.getListName();
        if (jsonObject.has(listName)) {
            if (jsonObject.get(listName).isJsonArray()) {
                listArray = jsonObject.getAsJsonArray(listName);
            } else {
                throw new JsonParseException(listName + " is not a JsonArray.");
            }
        } else {
            throw new JsonParseException(listName + " not found.");
        }
        return new ArrayList<>(listArray.asList().stream()
                .map(JsonElement::getAsString)
                .toList());
    }

    private static <T> void addBasicVisionValue(VisionProperty<BasicVisionValue<T>, T> property, T value, JsonObject arrayObject, JsonObject jsonObject, String key) {
        BasicVisionValue<T> basicVisionValue = new BasicVisionValue<>(value, VisionConditions.resolveConditions(arrayObject));
        basicVisionValue.setPriority(getPriority(jsonObject, key));
        property.addValue(basicVisionValue);
    }

    public static int getPriority(JsonObject jsonObject, String key) throws JsonParseException {
        String priorityName = key + "/priority";
        if (jsonObject.has(priorityName)) {
            if (!jsonObject.get(priorityName).isJsonPrimitive()) {
                throw new JsonParseException(key + " priority value is not a JsonPrimitive.");
            }
            JsonPrimitive jsonPrimitive = jsonObject.get(priorityName).getAsJsonPrimitive();
            return jsonPrimitive.getAsInt();
        }
        return 0;
    }

    private static boolean isValidListKey(String matchKey) {
        return matchKey.matches("[a-z0-9:!#*/_]+");
    }

    public static boolean visionApplies(@Nullable Object object, String id, List<String> applicantList, @Nullable ICondition.IContext context) {
        boolean invalidMatch = false;
        boolean validMatchFound = false;

        String[] idParts = id.split(":", 2);
        String idNamespace = idParts.length > 1 ? idParts[0] : "";
        String idPath = idParts.length > 1 ? idParts[1] : idParts[0];

        for (String matchKey : applicantList) {
            boolean inverted = matchKey.startsWith("!");
            if (inverted) matchKey = matchKey.substring(1);

            boolean found = false;
            boolean isTag = matchKey.startsWith("#");
            if (isTag) matchKey = matchKey.substring(1);

            String[] matchParts = matchKey.split(":", 2);
            String matchNamespace = matchParts.length > 1 ? matchParts[0] : "";
            String matchPath = matchParts.length > 1 ? matchParts[1] : matchParts[0];


            if (matchKey.equals("all") || wildcardMatches(id, matchKey)) {
                found = true;
            } else if (matchNamespace.isEmpty()) {
                if (idPath.equals(matchPath) || wildcardMatches(idPath, matchPath)) {
                    found = true;
                }
            } else if (id.equals(matchKey) || wildcardMatches(id, matchKey)) {
                found = true;
            } else if (isTag) {
                ResourceLocation tagLocation = new ResourceLocation(matchKey);
                if (object instanceof Item item) {
                    found = isItemTagged(item, tagLocation, context);
                } else if (object instanceof Block block) {
                    found = isBlockTagged(block, tagLocation, context);
                } else if (object instanceof EntityType<?> entityType) {
                    found = isEntityTagged(entityType, tagLocation, context);
                }
            }

            if (inverted) found = !found;

            if (!inverted && found) {
                validMatchFound = true;
            } else if (inverted && !found) {
                invalidMatch = true;
                break;
            }
        }

        return !invalidMatch && validMatchFound;
    }


    private static boolean wildcardMatches(String value, String pattern) {
        if (pattern.equals("*")) return true;

        if (pattern.startsWith("*") && pattern.endsWith("*")) {
            return value.contains(pattern.substring(1, pattern.length() - 1));
        } else if (pattern.startsWith("*")) {
            return value.endsWith(pattern.substring(1));
        } else if (pattern.endsWith("*")) {
            return value.startsWith(pattern.substring(0, pattern.length() - 1));
        }

        return value.equals(pattern);
    }


    private static boolean isItemTagged(Item item, ResourceLocation matchKey, @Nullable ICondition.IContext context) {
        if (context == null)
            return false;

        TagKey<Item> itemTagKey = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), matchKey);
        Collection<Holder<Item>> tags = context.getTag(itemTagKey);

        if (tags == null || tags.isEmpty())
            return false;

        ResourceLocation itemKey = ForgeRegistries.ITEMS.getKey(item);

        if (itemKey == null)
            return false;

        return tags.stream().anyMatch(holder -> {
            ResourceLocation holderKey = ForgeRegistries.ITEMS.getKey(holder.value());
            return Objects.equals(holderKey, itemKey);
        });

    }

    private static boolean isBlockTagged(Block block, ResourceLocation matchKey, @Nullable ICondition.IContext context) {
        if (context == null)
            return false;

        TagKey<Block> blockTagKey = TagKey.create(ForgeRegistries.BLOCKS.getRegistryKey(), matchKey);
        Collection<Holder<Block>> tags = context.getTag(blockTagKey);

        if (tags == null || tags.isEmpty())
            return false;

        ResourceLocation blockKey = ForgeRegistries.BLOCKS.getKey(block);

        if (blockKey == null)
            return false;

        return tags.stream().anyMatch(holder -> {
            ResourceLocation holderKey = ForgeRegistries.BLOCKS.getKey(holder.value());
            return Objects.equals(holderKey, blockKey);
        });
    }

    private static boolean isEntityTagged(EntityType<?> entityType, ResourceLocation matchKey, @Nullable ICondition.IContext context) {
        if (context == null)
            return false;

        TagKey<EntityType<?>> entityTagKey = TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), matchKey);
        Collection<Holder<EntityType<?>>> tags = context.getTag(entityTagKey);

        if (tags == null || tags.isEmpty())
            return false;

        ResourceLocation entityKey = ForgeRegistries.ENTITY_TYPES.getKey(entityType);

        if (entityKey == null)
            return false;

        return tags.stream().anyMatch(holder -> {
            ResourceLocation holderKey = ForgeRegistries.ENTITY_TYPES.getKey(holder.value());
            return Objects.equals(holderKey, entityKey);
        });
    }

    /*
    private void parseStringVisionProperty(JsonObject jsonObject, String key, VisionProperty<StringVisionValue, String> property) {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            JsonArray conditionArray = arrayObject.getAsJsonArray("conditions");
            String value = arrayObject.getAsJsonPrimitive("value").getAsString();

            List<String> conditionList = conditionArray.asList().stream()
                    .map(JsonElement::getAsString)
                    .toList();

            property.addValue(new StringVisionValue(value, conditionList));
            VMinus.LOGGER.info("{} = {}", key, value);
        }
    }

     */

}
