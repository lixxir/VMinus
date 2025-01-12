package net.lixir.vminus.visions.resources;

import com.google.common.collect.Maps;
import com.google.gson.*;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

public class VisionManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    private final String directory;
    private final VisionType visionType;
    private final ICondition.IContext context;

    private static final int PATH_SUFFIX_LENGTH = ".json".length();

    public VisionManager(VisionType visionType,  ICondition.IContext context) {
        super(GSON, "");
        this.directory = visionType.getDirectoryName();
        this.visionType = visionType;
        this.context = context;
    }

    // Had to override, so I could use the directory from the vision type instead.
    @Override
    protected @NotNull Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        Map<ResourceLocation, JsonElement> map = Maps.newHashMap();
        int i = this.directory.length() + 1;

        for(Map.Entry<ResourceLocation, Resource> entry : resourceManager.listResources(this.directory, (p_215600_) -> p_215600_.getPath().endsWith(".json")).entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            String s = resourcelocation.getPath();
            ResourceLocation resourcelocation1 = new ResourceLocation(resourcelocation.getNamespace(), s.substring(i, s.length() - PATH_SUFFIX_LENGTH));

            try {
                Reader reader = entry.getValue().openAsReader();

                try {
                    JsonElement jsonelement = GsonHelper.fromJson(GSON, reader, JsonElement.class);
                    JsonElement jsonelement1 = map.put(resourcelocation1, jsonelement);
                    if (jsonelement1 != null) {
                        throw new IllegalStateException("Duplicate data file ignored with ID " + resourcelocation1);
                    }
                } catch (Throwable throwable1) {
                    try {
                        reader.close();
                    } catch (Throwable throwable) {
                        throwable1.addSuppressed(throwable);
                    }

                    throw throwable1;
                }

                reader.close();
            } catch (IllegalArgumentException | IOException | JsonParseException jsonParseException) {
                VMinusMod.LOGGER.error("Couldn't parse data file {} from {}", resourcelocation1, resourcelocation, jsonParseException);
            }
        }

        return map;
    }


    @Override
    protected void apply(
            @NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap,
            @NotNull ResourceManager resourceManager,
            @NotNull ProfilerFiller profilerFiller) {

        JsonObject mainJsonObject = new JsonObject();

        for (Map.Entry<ResourceLocation, JsonElement> jsonFile : resourceLocationJsonElementMap.entrySet()) {
            try {
                JsonObject jsonObject = VisionResourceHandler.processJsonObject(this.directory, jsonFile.getValue());

                for (String key : jsonObject.keySet()) {
                    JsonElement value = jsonObject.get(key);

                    if (mainJsonObject.has(key)) {
                        JsonObject existingObject = mainJsonObject.getAsJsonObject(key);
                        JsonObject newObject = value.getAsJsonObject();

                        VisionResourceHandler.mergeJsonObjects(existingObject, newObject, 0);
                    } else {
                        mainJsonObject.add(key, value);
                    }
                }
            } catch (Exception e) {
                VMinusMod.LOGGER.error("Error processing JSON file: {}", jsonFile.getKey(), e);
            }
        }

        // Processing for configs.
        File configDir = new File("config/vminus/" + directory);
        if (!configDir.exists() && !configDir.mkdirs()) {
            VMinusMod.LOGGER.error("Failed to create directory: {}", configDir.getPath());
        }
        if (configDir.exists() && configDir.isDirectory()) {
            File[] jsonFiles = configDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (jsonFiles != null) {
                for (File file : jsonFiles) {
                    try (FileReader reader = new FileReader(file)) {
                        JsonObject configJson = JsonParser.parseReader(reader).getAsJsonObject();
                        try {
                            JsonObject jsonObject = VisionResourceHandler.processJsonObject(this.directory, configJson);

                            for (String key : jsonObject.keySet()) {
                                JsonElement value = jsonObject.get(key);

                                if (mainJsonObject.has(key)) {
                                    JsonObject existingObject = mainJsonObject.getAsJsonObject(key);
                                    JsonObject newObject = value.getAsJsonObject();

                                    VisionResourceHandler.mergeJsonObjects(existingObject, newObject, 0);
                                } else {
                                    mainJsonObject.add(key, value);
                                }
                            }
                        } catch (Exception e) {
                            VMinusMod.LOGGER.error("Error processing JSON file: {}", file.getName(), e);
                        }
                    } catch (IOException e) {
                        VMinusMod.LOGGER.error("Error reading config: {}", file.getName(), e);
                    }
                }
            }
        }
        this.visionType.getVisionKey().clear();
        this.visionType.getVisionCache().clear();

        this.visionType.setMainVision(mainJsonObject);

        VisionHandler.processAllVisionDataForType(this.visionType,context);

        this.visionType.clearMainVision();
    }

    // Needed to override for the correct directory.
    @Override
    protected @NotNull ResourceLocation getPreparedPath(ResourceLocation rl) {
        return new ResourceLocation(rl.getNamespace(), this.directory + "/" + rl.getPath() + ".json");
    }
}