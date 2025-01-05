package net.lixir.vminus.visions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class ResourceVisionLoader {
    public static void generateItemVisionsFile(LevelAccessor world) {
        if (world.isClientSide())
            return;
        if (world instanceof ServerLevel serverLevel_) {
            VminusModVariables.main_item_vision = mergeModifiersFromResourcePacks(serverLevel_, "item_visions");
        }
    }

    public static void generateBlockVisionsFile(LevelAccessor world) {
        if (world.isClientSide())
            return;
        if (world instanceof ServerLevel serverLevel_) {
            VminusModVariables.main_block_vision = mergeModifiersFromResourcePacks(serverLevel_, "block_visions");
        }
    }

    public static void generateEntityVisionsFile(LevelAccessor world) {
        if (world.isClientSide())
            return;
        if (world instanceof ServerLevel serverLevel_) {
            VminusModVariables.main_entity_vision = mergeModifiersFromResourcePacks(serverLevel_, "entity_visions");
        }
    }

    public static void generateEffectVisionsFile(LevelAccessor world) {
        if (world.isClientSide())
            return;
        if (world instanceof ServerLevel serverLevel_) {
            VminusModVariables.main_effect_vision = mergeModifiersFromResourcePacks(serverLevel_, "effect_visions");
        }
    }

    public static void generateEnchantmentVisionsFile(LevelAccessor world) {
        if (world.isClientSide())
            return;
        if (world instanceof ServerLevel serverLevel_) {
            VminusModVariables.main_enchantment_vision = mergeModifiersFromResourcePacks(serverLevel_, "enchantment_visions");
        }
    }

    public static JsonObject mergeModifiersFromResourcePacks(ServerLevel serverLevel, String folderName) {
        JsonObject jsonObject = new JsonObject();
        ResourceManager resourceManager = serverLevel.getServer().getResourceManager();

        resourceManager.listPacks().forEach(pack -> {
            pack.getNamespaces(PackType.SERVER_DATA).forEach(namespace -> {
                resourceManager.getResourceStack(new ResourceLocation(namespace, folderName))
                        .forEach(resource -> {
                            try (InputStream inputStream = resource.open()) {
                                String jsonString = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                                        .lines().collect(Collectors.joining("\n"));
                                JsonObject newJsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
                                processJsonFolder(newJsonObject, folderName, jsonObject);
                            } catch (IOException e) {
                                VMinusMod.LOGGER.error("Error reading resource in folder {}", folderName, e);
                            }
                        });
            });
        });

        File configDir = new File("config/vminus/" + folderName);
        if (configDir.exists() && configDir.isDirectory()) {
            File[] jsonFiles = configDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (jsonFiles != null) {
                for (File file : jsonFiles) {
                    try (FileReader reader = new FileReader(file)) {
                        JsonObject configJson = JsonParser.parseReader(reader).getAsJsonObject();
                        processJsonFolder(configJson, folderName, jsonObject);
                    } catch (IOException e) {
                        VMinusMod.LOGGER.error("Error reading config: {}", file.getName(), e);
                    }
                }
            }
        }

        return jsonObject;
    }

    private static void processJsonFolder(JsonObject newJsonObject, String folderName, JsonObject jsonObject) {
        String listType = switch (folderName) {
            case "item_visions" -> "items";
            case "block_visions" -> "blocks";
            case "entity_visions" -> "entities";
            case "enchantment_visions" -> "enchantments";
            case "effect_visions" -> "effects";
            default -> "";
        };

        if (!listType.isEmpty()) {
            newJsonObject = transformArrayKeyJson(newJsonObject, listType);
        }

        for (String key : newJsonObject.keySet()) {
            JsonElement newElement = newJsonObject.get(key);
            JsonElement wrappedElement = wrapPrimitive(key, newElement);

            if (jsonObject.has(key)) {
                JsonObject existingObject = jsonObject.getAsJsonObject(key);
                int existingPriority = getPriorityFromWrapped(existingObject);
                int newPriority = getPriorityFromWrapped(wrappedElement.getAsJsonObject());

                if (newPriority > existingPriority) {
                    jsonObject.remove(key);
                    jsonObject.add(key, wrappedElement);
                } else if (newPriority == existingPriority) {
                    mergeJsonObjects(existingObject, wrappedElement.getAsJsonObject(), 0);
                }
            } else {
                jsonObject.add(key, wrappedElement);
            }
        }
    }

    public static JsonObject transformArrayKeyJson(JsonObject inputJson, String listType) {
        if (inputJson.has(listType) && inputJson.get(listType).isJsonArray()) {
            JsonArray listArray = inputJson.getAsJsonArray(listType);
            StringBuilder combinedKey = new StringBuilder();

            for (int i = 0; i < listArray.size(); i++) {
                combinedKey.append(listArray.get(i).getAsString());
                if (i < listArray.size() - 1) {
                    combinedKey.append(",");
                }
            }
            JsonObject wrappedContent = new JsonObject();
            for (Map.Entry<String, JsonElement> entry : inputJson.entrySet()) {
                if (!entry.getKey().equals(listType)) {
                    wrappedContent.add(entry.getKey(), entry.getValue());
                }
            }

            JsonObject resultObject = new JsonObject();
            resultObject.add(combinedKey.toString(), wrappedContent);
            return resultObject;
        }
        return inputJson;
    }

    private static int getPriorityFromWrapped(JsonObject wrappedObject) {
        if (wrappedObject.has("priority")) {
            JsonElement priorityElement = wrappedObject.get("priority");

            if (priorityElement.isJsonObject()) {
                JsonObject priorityObject = priorityElement.getAsJsonObject();
                if (priorityObject.has("value")) {
                    return priorityObject.get("value").getAsInt();
                }
            } else if (priorityElement.isJsonArray()) {
                JsonArray priorityArray = priorityElement.getAsJsonArray();
                if (!priorityArray.isEmpty() && priorityArray.get(0).isJsonObject()) {
                    JsonObject firstPriorityObject = priorityArray.get(0).getAsJsonObject();
                    if (firstPriorityObject.has("value")) {
                        return firstPriorityObject.get("value").getAsInt();
                    }
                }
            }
        }
        return 0;
    }


    public static JsonElement wrapPrimitive(String key, @Nullable JsonElement element) {
        if (element != null) {
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                JsonObject wrappedObject = new JsonObject();
                for (String innerKey : obj.keySet()) {
                    JsonElement innerElement = obj.get(innerKey);
                    if (innerElement.isJsonPrimitive()) {
                        JsonObject valueObject = new JsonObject();
                        valueObject.add("value", innerElement);
                        JsonArray valueArray = new JsonArray();
                        valueArray.add(valueObject);
                        wrappedObject.add(innerKey, valueArray);
                    } else {
                        wrappedObject.add(innerKey, innerElement);
                    }
                }
                return wrappedObject;
            }
        }
        return element;
    }

    public static void mergeJsonObjects(JsonObject existingJsonObject, JsonObject newJsonObject, int depth) {
        for (String key : newJsonObject.keySet()) {
            JsonElement newElement = newJsonObject.get(key);
            if (existingJsonObject.has(key)) {
                JsonElement existingElement = existingJsonObject.get(key);
                if (existingElement.isJsonObject() && newElement.isJsonObject()) {
                    int existingPriority = getPriority(existingElement.getAsJsonObject());
                    int newPriority = getPriority(newElement.getAsJsonObject());

                    if (newPriority > existingPriority) {
                        existingJsonObject.add(key, newElement);
                    } else if (newPriority == existingPriority) {
                        mergeJsonObjects(existingElement.getAsJsonObject(), newElement.getAsJsonObject(), depth + 1);
                    }
                } else if (!existingElement.isJsonObject() && !newElement.isJsonObject()) {

                    existingJsonObject.add(key, newElement);
                } else {

                    VMinusMod.LOGGER.warn("Type mismatch for key: {}. Overwriting with new value.", key);
                    existingJsonObject.add(key, newElement);
                }
            } else {

                existingJsonObject.add(key, newElement);
            }
        }
    }

    private static int getPriority(JsonObject jsonObject) {
        if (jsonObject.has("value")) {
            JsonElement valueElement = jsonObject.get("value");
            if (valueElement.isJsonArray() && !valueElement.getAsJsonArray().isEmpty()) {
                JsonElement innerElement = valueElement.getAsJsonArray().get(0);
                if (innerElement.isJsonObject() && innerElement.getAsJsonObject().has("priority")) {
                    return innerElement.getAsJsonObject().get("priority").getAsInt();
                }
            }
        }
        return 500;
    }
}
