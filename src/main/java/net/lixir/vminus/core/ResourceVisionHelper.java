package net.lixir.vminus.core;

import com.google.gson.*;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

public class ResourceVisionHelper {
    public static void generateItemVisionsFile(LevelAccessor world) {
        if (world.isClientSide())
            return;
        if (world instanceof ServerLevel srvlvl_) {
            VminusModVariables.main_item_vision = mergeModifiersFromResourcePacks(srvlvl_, "item_visions");
        }
    }

    public static void generateBlockVisionsFile(LevelAccessor world) {
        if (world.isClientSide())
            return;
        if (world instanceof ServerLevel srvlvl_) {
            VminusModVariables.main_block_vision = mergeModifiersFromResourcePacks(srvlvl_, "block_visions");
        }
    }

    public static void generateEntityVisionsFile(LevelAccessor world) {
        if (world.isClientSide())
            return;
        if (world instanceof ServerLevel srvlvl_) {
            VminusModVariables.main_entity_vision = mergeModifiersFromResourcePacks(srvlvl_, "entity_visions");
        }
    }

    public static void generateEffectVisionsFile(LevelAccessor world) {
        if (world.isClientSide())
            return;
        if (world instanceof ServerLevel srvlvl_) {
            VminusModVariables.main_effect_vision = mergeModifiersFromResourcePacks(srvlvl_, "effect_visions");
        }
    }

    public static void generateEnchantmentVisionsFile(LevelAccessor world) {
        if (world.isClientSide())
            return;
        if (world instanceof ServerLevel srvlvl_) {
            VminusModVariables.main_enchantment_vision = mergeModifiersFromResourcePacks(srvlvl_, "enchantment_visions");
        }
    }

    public static JsonObject mergeModifiersFromResourcePacks(ServerLevel srvlvl_, String folderName) {
        JsonObject jsonObject = new JsonObject();
        class Output implements PackResources.ResourceOutput {
            private final JsonObject jsonObject;

            public Output(JsonObject jsonObject) {
                this.jsonObject = jsonObject;
            }

            @Override
            public void accept(ResourceLocation resourceLocation, IoSupplier<InputStream> ioSupplier) {
                try {

                    String jsonString = new java.io.BufferedReader(new java.io.InputStreamReader(ioSupplier.get(), java.nio.charset.StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                    JsonObject newJsonObject = new Gson().fromJson(jsonString, JsonObject.class);
                    if (newJsonObject == null) {
                        VMinusMod.LOGGER.warn("Failed to parse JSON from resource vision: " + resourceLocation);
                        return;
                    }
                    for (String key : newJsonObject.keySet()) {
                        JsonElement newElement = newJsonObject.get(key);
                        JsonElement wrappedElement = wrapPrimitive(key, newElement);

                        if (this.jsonObject.has(key)) {
                            JsonObject existingObject = this.jsonObject.getAsJsonObject(key);

                            for (String key3 : existingObject.keySet()) {
                                JsonElement element = existingObject.get(key3);
                                wrapPrimitive(key3, element);
                            }

                            int existingPriority = getPriorityFromWrapped(existingObject);
                            int newPriority = getPriorityFromWrapped(wrappedElement.getAsJsonObject());

                            if (newPriority > existingPriority) {
                                this.jsonObject.remove(key);
                                this.jsonObject.add(key, wrappedElement);
                            } else if (newPriority == existingPriority) {
                                mergeJsonObjects(existingObject, wrappedElement.getAsJsonObject(), 0);
                            }
                        } else {
                            this.jsonObject.add(key, wrappedElement);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    VMinusMod.LOGGER.error("Error processing resource vision vision: " + resourceLocation, e);
                }
            }
        }
        Output output = new Output(jsonObject);
        ResourceManager rm = srvlvl_.getServer().getResourceManager();
        rm.listPacks().forEach(resource -> {
            for (String namespace : resource.getNamespaces(PackType.SERVER_DATA)) {
                resource.listResources(PackType.SERVER_DATA, namespace, folderName, output);
            }
        });
        File configDir = new File("config/vminus/" + folderName);
        if (!configDir.exists()) {
            if (configDir.mkdirs()) {
                VMinusMod.LOGGER.info("Created directory: " + configDir.getPath());
            } else {
                VMinusMod.LOGGER.error("Failed to create directory: " + configDir.getPath());
            }
        }
        if (configDir.exists() && configDir.isDirectory()) {
            File[] jsonFiles = configDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (jsonFiles != null) {
                for (File file : jsonFiles) {
                    try (FileReader reader = new FileReader(file)) {
                        JsonObject configJson = JsonParser.parseReader(reader).getAsJsonObject();
                        for (String key : configJson.keySet()) {
                            if (jsonObject.has(key)) {
                                for (String key2 : configJson.getAsJsonObject(key).keySet()) {
                                    wrapPrimitive(key2, configJson.getAsJsonObject(key2));
                                }
                                for (String key3 : jsonObject.getAsJsonObject(key).keySet()) {
                                    wrapPrimitive(key3, jsonObject.getAsJsonObject(key3));
                                }
                                mergeJsonObjects(jsonObject.getAsJsonObject(key), configJson.getAsJsonObject(key), 0);
                            } else {
                                JsonElement wrappedElement = wrapPrimitive(key, configJson.get(key));
                                jsonObject.add(key, wrappedElement);
                            }
                        }
                    } catch (IOException e) {
                        VMinusMod.LOGGER.error("Error reading config vision: " + file.getName(), e);
                    }
                }
            }
        }
        return jsonObject;
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
                if (priorityArray.size() > 0 && priorityArray.get(0).isJsonObject()) {
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

                    VMinusMod.LOGGER.warn("Type mismatch for key: " + key + ". Overwriting with new value.");
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
            if (valueElement.isJsonArray() && valueElement.getAsJsonArray().size() > 0) {
                JsonElement innerElement = valueElement.getAsJsonArray().get(0);
                if (innerElement.isJsonObject() && innerElement.getAsJsonObject().has("priority")) {
                    return innerElement.getAsJsonObject().get("priority").getAsInt();
                }
            }
        }
        return 500;
    }

}
