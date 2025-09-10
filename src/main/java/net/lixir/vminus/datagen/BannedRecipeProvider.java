package net.lixir.vminus.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.lixir.vminus.VMinus;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class BannedRecipeProvider implements DataProvider {
    private final PackOutput output;
    protected final String modId;
    private final Set<ResourceLocation> bannedRecipes = new HashSet<>();

    public BannedRecipeProvider(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    protected abstract void addBannedRecipes();

    protected void ban(ResourceLocation recipeId) {
        bannedRecipes.add(recipeId);
    }

    protected void ban(String path) {
        ban(new ResourceLocation(modId, path));
    }

    protected void ban(String namespace, String path) {
        ban(new ResourceLocation(namespace, path));
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        addBannedRecipes();

        JsonObject json = new JsonObject();
        JsonArray array = new JsonArray();
        bannedRecipes.forEach(id -> array.add(id.toString()));
        json.add("banned", array);

        Path path = output.getOutputFolder()
                .resolve("data/" + modId + "/bans/recipes/banned.json");

        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public @NotNull String getName() {
        return "Banned Recipe Provider [" + modId + "]";
    }

    public Set<ResourceLocation> getBannedRecipes() {
        return Collections.unmodifiableSet(bannedRecipes);
    }
}
