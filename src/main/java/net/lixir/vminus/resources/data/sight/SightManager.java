package net.lixir.vminus.resources.data.sight;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.VMinus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SightManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, Boolean> SIGHTS = new HashMap<>();
    private static final Map<String, Integer> PRIORITIES = new HashMap<>();

    public SightManager() {
        super(GSON, "sights");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> files, ResourceManager manager, ProfilerFiller profiler) {
        SIGHTS.clear();
        PRIORITIES.clear();
        // Load datapack JSONs
        for (Map.Entry<ResourceLocation, JsonElement> entry : files.entrySet()) {
            processJson(entry.getValue().getAsJsonObject());
        }

        // Load config directory JSONs
        File configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "sights");
        if (configDir.exists() && configDir.isDirectory()) {
            File[] jsonFiles = configDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (jsonFiles != null) {
                for (File file : jsonFiles) {
                    try (Reader reader = new FileReader(file)) {
                        JsonObject json = GSON.fromJson(reader, JsonObject.class);
                        processJson(json);
                    } catch (Exception e) {
                        VMinus.LOGGER.error("Error reading config file '{}':", file.getName(), e);
                    }
                }
            }
        }
        VMinus.LOGGER.info("Loaded {} sights", SIGHTS.size());
    }

    private void processJson(@NotNull JsonObject json) {
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String key = entry.getKey();
            if (key.endsWith("/priority"))
                continue;

            boolean value = entry.getValue().getAsBoolean();
            int priority = json.has(key + "/priority") ? json.get(key + "/priority").getAsInt() : 0;

            if (!PRIORITIES.containsKey(key) || priority >= PRIORITIES.get(key)) {
                PRIORITIES.put(key, priority);
                SIGHTS.put(key, value);
            }
        }
    }

    public static boolean get(String id) {
        return SIGHTS.getOrDefault(id, false);
    }

    public static @NotNull @UnmodifiableView Map<String, Boolean> getAllSights() {
        return Collections.unmodifiableMap(SIGHTS);
    }

    public static void setAll(Map<String, Boolean> newSights) {
        SIGHTS.clear();
        PRIORITIES.clear();
        SIGHTS.putAll(newSights);

    }
}
