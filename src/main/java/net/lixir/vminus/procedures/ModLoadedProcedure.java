package net.lixir.vminus.procedures;

import com.google.gson.*;
import net.lixir.vminus.VminusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class ModLoadedProcedure {
    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        execute(event.getLevel());
    }

    public static void execute(LevelAccessor world) {
        if (world.isClientSide())
            return;
        execute(null, world);
    }

    public static void execute(@Nullable Event event, LevelAccessor world) {
        if (world.isClientSide())
            return;
        generateItemVisionsFile(world);
        generateBlockVisionsFile(world);
        generateEntityVisionsFile(world);
        generateEffectVisionsFile(world);
        generateEnchantmentVisionsFile(world);
    }

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

    private static JsonObject mergeModifiersFromResourcePacks(ServerLevel srvlvl_, String folderName) {
        JsonObject jsonObject = new JsonObject();
        class Output implements PackResources.ResourceOutput {
            private final JsonObject jsonObject;

            public Output(JsonObject jsonObject) {
                this.jsonObject = jsonObject;
            }

            @Override
            public void accept(ResourceLocation resourceLocation, IoSupplier<InputStream> ioSupplier) {
                try {
                    VminusMod.LOGGER.info("Processing resource vision: " + resourceLocation);
                    String jsonString = new java.io.BufferedReader(new java.io.InputStreamReader(ioSupplier.get(), java.nio.charset.StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                    JsonObject newJsonObject = new Gson().fromJson(jsonString, JsonObject.class);
                    if (newJsonObject == null) {
                        VminusMod.LOGGER.warn("Failed to parse JSON from resource vision: " + resourceLocation);
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
                            mergeJsonObjects(existingObject, wrappedElement.getAsJsonObject(), 0);
                        } else {
                            this.jsonObject.add(key, wrappedElement);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    VminusMod.LOGGER.error("Error processing resource vision vision: " + resourceLocation, e);
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
                VminusMod.LOGGER.info("Created directory: " + configDir.getPath());
            } else {
                VminusMod.LOGGER.error("Failed to create directory: " + configDir.getPath());
            }
        }
        if (configDir.exists() && configDir.isDirectory()) {
            File[] jsonFiles = configDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (jsonFiles != null) {
                for (File file : jsonFiles) {
                    VminusMod.LOGGER.info("Processing config vision: " + file.getName());
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
                        VminusMod.LOGGER.error("Error reading config vision: " + file.getName(), e);
                    }
                }
            }
        }
        return jsonObject;
    }

    private static JsonElement wrapPrimitive(String key, @Nullable JsonElement element) {
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

    private static void mergeJsonObjects(JsonObject existingJsonObject, JsonObject newJsonObject, int depth) {
        for (String key : newJsonObject.keySet()) {
            JsonElement newElement = newJsonObject.get(key);
            if (existingJsonObject.has(key)) {
                JsonElement existingElement = existingJsonObject.get(key);
                if (newElement.isJsonObject() && existingElement.isJsonObject()) {
                    mergeJsonObjects(existingElement.getAsJsonObject(), newElement.getAsJsonObject(), depth + 1);
                } else if (!existingElement.equals(newElement)) {
                    existingJsonObject.add(key, newElement);
                }
            } else {
                existingJsonObject.add(key, newElement);
            }
        }
    }
}
