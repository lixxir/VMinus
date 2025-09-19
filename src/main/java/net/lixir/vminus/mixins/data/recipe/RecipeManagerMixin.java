package net.lixir.vminus.mixins.data.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.lixir.vminus.resources.data.RegistryAccessHolder;
import net.lixir.vminus.vision.util.VisionUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin extends SimpleJsonResourceReloadListener implements RegistryAccessHolder {
    @Shadow
    @Mutable
    private Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes;

    @Shadow
    @Mutable
    private Map<ResourceLocation, Recipe<?>> byName;

    public RecipeManagerMixin(Gson gson, String id) {
        super(gson, id);
    }

    @Unique
    private RegistryAccess.Frozen vMinus$registryAccess;

    @Override
    public RegistryAccess.Frozen vMinus$getRegistryAccess() {
        return vMinus$registryAccess;
    }

    @Override
    public void vMinus$setRegistryAccess(RegistryAccess.Frozen access) {
        this.vMinus$registryAccess = access;
    }

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("TAIL"))
    private void vMinus$filterBannedRecipes(Map<ResourceLocation, JsonElement> p_44037_, ResourceManager resourceManager, ProfilerFiller p_44039_, CallbackInfo ci) {
        Predicate<Map.Entry<ResourceLocation, Recipe<?>>> recipeFilter = e ->
                !VisionUtils.isRecipeBanned(e.getValue(), e.getKey(), vMinus$registryAccess);

        Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> filtered = new HashMap<>();
        for (Map.Entry<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> entry : this.recipes.entrySet()) {
            Map<ResourceLocation, Recipe<?>> typeMap = entry.getValue().entrySet().stream()
                    .filter(recipeFilter)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            filtered.put(entry.getKey(), typeMap);
        }
        this.recipes = filtered;
        this.byName = this.byName.entrySet().stream()
                .filter(recipeFilter)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
