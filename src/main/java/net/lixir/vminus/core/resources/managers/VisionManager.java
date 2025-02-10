package net.lixir.vminus.core.resources.managers;

import com.google.gson.*;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.core.resources.VisionProcessor;
import net.lixir.vminus.events.LevelLoadedEventHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public abstract class VisionManager<T> extends SimpleJsonResourceReloadListener {
    private final ICondition.IContext context;
    private final String directory;
    private final Class<T> visionClass;
    private final Gson gson;

    protected VisionManager(ICondition.IContext context, String directory, Class<T> visionClass, JsonDeserializer<T> deserializer) {
        super(new GsonBuilder().registerTypeAdapter(visionClass, deserializer).setPrettyPrinting().disableHtmlEscaping().create(), directory);
        this.context = context;
        this.directory = directory;
        this.visionClass = visionClass;
        this.gson = new GsonBuilder().registerTypeAdapter(visionClass, deserializer).setPrettyPrinting().disableHtmlEscaping().create();
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        ArrayList<T> visions = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonElement> jsonFile : resourceLocationJsonElementMap.entrySet()) {
            try {
                JsonObject jsonObject = VisionProcessor.processJson(directory, jsonFile.getValue());
                visions.add(gson.fromJson(jsonObject, visionClass));
            } catch (Exception e) {
                VMinus.LOGGER.error("Error processing JSON file for {}: {}", visionClass.getSimpleName(), jsonFile.getKey(), e);
            }
        }

        File configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), directory);
        if (configDir.exists() && configDir.isDirectory()) {
            File[] jsonFiles = configDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (jsonFiles != null) {
                for (File jsonFile : jsonFiles) {
                    try (Reader reader = new FileReader(jsonFile)) {
                        JsonObject jsonObject = VisionProcessor.processJson(directory, JsonParser.parseReader(reader));
                        visions.add(gson.fromJson(jsonObject, visionClass));
                    } catch (Exception e) {
                        VMinus.LOGGER.error("Error processing config file for {}: {}", visionClass.getSimpleName(), jsonFile.getName(), e);
                    }
                }
            }
        }

        applyVisions(visions);
        LevelLoadedEventHandler.debounce = false;
    }

    protected abstract void applyVisions(List<T> visions);

    public ICondition.IContext getContext() {
        return context;
    }
}
