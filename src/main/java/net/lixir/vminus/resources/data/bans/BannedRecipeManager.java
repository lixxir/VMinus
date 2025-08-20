package net.lixir.vminus.resources.data.bans;

import com.google.gson.*;
import net.lixir.vminus.VMinus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BannedRecipeManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final BannedRecipeManager INSTANCE = new BannedRecipeManager();

    private final Set<ResourceLocation> bannedRecipes = new HashSet<>();

    private BannedRecipeManager() {
        super(GSON, "bans/recipes");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> objects,
                         ResourceManager resourceManager,
                         ProfilerFiller profiler) {
        bannedRecipes.clear();

        // Load datapack JSONs
        for (Map.Entry<ResourceLocation, JsonElement> entry : objects.entrySet()) {
            processJson(entry.getValue().getAsJsonObject());
        }

        // Load config directory JSONs
        File configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "bans/recipes");
        if (configDir.exists() && configDir.isDirectory()) {
            File[] jsonFiles = configDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (jsonFiles != null) {
                for (File file : jsonFiles) {
                    try (Reader reader = new FileReader(file)) {
                        JsonObject json = GSON.fromJson(reader, JsonObject.class);
                        processJson(json);
                    } catch (Exception e) {
                        VMinus.LOGGER.error("Error reading recipe ban config file '{}':", file.getName(), e);
                    }
                }
            }
        }

        VMinus.LOGGER.info("Loaded {} banned recipes", bannedRecipes.size());
    }

    private void processJson(@NotNull JsonObject obj) {
        if (!obj.has("banned"))
            return;
        JsonArray arr = obj.getAsJsonArray("banned");
        for (JsonElement e : arr) {
            bannedRecipes.add(new ResourceLocation(e.getAsString()));
        }
    }

    public boolean isBanned(ResourceLocation recipeId) {
        return bannedRecipes.contains(recipeId);
    }

    public Set<ResourceLocation> getBannedRecipes() {
        return Collections.unmodifiableSet(bannedRecipes);
    }
}
