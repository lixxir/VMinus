package net.lixir.vminus.visions.resources;

import com.google.gson.*;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.visions.VisionType;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.ICondition;
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

        // Wrap single keys in a list
        keyToArray(jsonFileObject, singleName, listType);
        keyToArray(jsonFileObject, "tag", listType);

        JsonObject processedJsonObject = new JsonObject();

        // Add the list back if it existed.
        if (jsonFileObject.has(listType) && jsonFileObject.get(listType).isJsonArray())
            processedJsonObject.add(listType, jsonFileObject.get(listType));

        JsonArray variables = getOrCreateArray(jsonFileObject, "variables");
        JsonObject groups = getOrCreateObject(jsonFileObject, "groups");
        JsonArray globalConditions = getOrCreateArray(jsonFileObject, "conditions");
        globalConditions = processConditions(globalConditions, groups);

        for (Map.Entry<String, JsonElement> entry : jsonFileObject.entrySet()) {
            String key = entry.getKey();
            JsonElement jsonElement = entry.getValue();

            JsonArray mergedConditions = new JsonArray();

            // Merge global and local conditions together and process them
            if (jsonFileObject.has(key + "/conditions")) {
                JsonElement conditionElement = jsonFileObject.get(key + "/conditions");
                JsonArray conditionsArray;
                if (conditionElement.isJsonObject()) { // Wrapping single conditions
                    conditionsArray = new JsonArray();
                    conditionsArray.add(conditionElement);
                } else if (conditionElement.isJsonArray()) {
                    conditionsArray = conditionElement.getAsJsonArray();
                } else {
                    throw new IllegalStateException("Unexpected type for conditions: " + conditionElement.getClass());
                }

                conditionsArray = processConditions(conditionsArray, groups);
                mergedConditions.addAll(conditionsArray);
            }
            mergedConditions.addAll(globalConditions);


            if (jsonElement.isJsonPrimitive() && !key.endsWith("/priority")) { // Wrap primitives in arrays
                JsonObject wrappedObject = new JsonObject();

                if (key.equals("decorator") || key.equals("variant")) {
                    wrappedObject.add("texture", jsonElement);
                } else {
                    wrappedObject.add("value", jsonElement);
                }

                wrappedObject.add("conditions", mergedConditions);

                JsonArray jsonArray = new JsonArray();
                jsonArray.add(wrappedObject);

                processedJsonObject.add(key, jsonArray);
            } else if (jsonElement.isJsonArray() && !key.equals(listType) && !key.endsWith("/conditions") && !key.equals("conditions") && !key.equals("groups")) { // Wrap primitives in arrays in objects
                JsonArray newArray = new JsonArray();
                for (JsonElement arrayElement : jsonElement.getAsJsonArray()) {
                    if (arrayElement.isJsonPrimitive()) {
                        JsonObject wrappedObject = new JsonObject();
                        wrappedObject.add("value", arrayElement);
                        wrappedObject.add("conditions", mergedConditions);
                        newArray.add(wrappedObject);
                    } else if (arrayElement.isJsonObject()) {
                        JsonObject newObject = arrayElement.getAsJsonObject();
                        if (newObject.has("conditions")) {
                            newObject.get("conditions").getAsJsonArray().addAll(mergedConditions);
                        } else {
                            newObject.add("conditions", mergedConditions);
                        }

                        newArray.add(newObject);
                    } else {
                        newArray.add(arrayElement);
                    }
                }
                processedJsonObject.add(key, newArray);

            } else if (jsonElement.isJsonObject() && !key.endsWith("/conditions") && !key.equals("conditions") && !key.equals("groups") ) { // Wrap objects in arrays
                JsonArray newArray = wrapObjectInArray(jsonElement, mergedConditions);

                processedJsonObject.add(key, newArray);
            }
        }
        processedJsonObject.add("groups", groups);

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
        VMinus.LOGGER.info("PROCESSED JSON: {}", processedJsonObject);
        return processedJsonObject;
    }

    private static JsonArray processConditions(JsonArray conditionsArray, JsonObject groups) {
        // Collect the loose objects in the conditions and create a new condition group for them
        JsonArray processedArray = new JsonArray();
        ArrayList<JsonObject> looseObjects = new ArrayList<>();
        for (JsonElement conditionArrayElement : conditionsArray.asList()) {
            if (!conditionArrayElement.isJsonObject()) {
                processedArray.add(conditionArrayElement);
                continue;
            }
            looseObjects.add(conditionArrayElement.getAsJsonObject());
        }
        if (!looseObjects.isEmpty()) {
            UUID uuid;
            String uuidString;
            do {
                uuid = UUID.randomUUID();
                uuidString = uuid.toString();
            } while (groups.has(uuidString));
            JsonArray looseObjectJsonArray = new JsonArray();
            for (JsonObject looseObject : looseObjects)
                looseObjectJsonArray.add(looseObject);
            groups.add(uuidString, looseObjectJsonArray);
            processedArray.add(uuidString);
        }
        return processedArray;
    }

    private static @NotNull JsonObject getOrCreateObject(JsonObject jsonFileObject, String string){
        JsonObject jsonObject;
        if (jsonFileObject.has(string)) {
            jsonObject = jsonFileObject.getAsJsonObject(string);
        } else {
            jsonObject = new JsonObject();
        }
        return jsonObject;
    }


    private static @NotNull JsonArray getOrCreateArray(JsonObject jsonFileObject, String string){
        JsonArray jsonArray;
        if (jsonFileObject.has(string)) {
            JsonElement jsonElement = jsonFileObject.get(string);
            if (jsonElement.isJsonObject()) {
                jsonArray = new JsonArray();
                jsonArray.add(jsonElement.getAsJsonObject());
            } else if (jsonElement.isJsonArray()) {
                jsonArray = jsonElement.getAsJsonArray();
            } else {
                jsonArray = new JsonArray();
            }
        } else {
            jsonArray = new JsonArray();
        }
        return jsonArray;
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
}
