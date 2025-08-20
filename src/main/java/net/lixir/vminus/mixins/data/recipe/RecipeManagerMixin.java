package net.lixir.vminus.mixins.data.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.resources.data.RegistryAccessHolder;
import net.lixir.vminus.vision.util.VisionUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

    public RecipeManagerMixin(Gson p_10768_, String p_10769_) {
        super(p_10768_, p_10769_);
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

    @Inject(method = "createCheck", at = @At("RETURN"), cancellable = true)
    private static <C extends Container, T extends Recipe<C>> void vminus$filterBannedRecipes(
            RecipeType<T> type,
            @NotNull CallbackInfoReturnable<RecipeManager.CachedCheck<C, T>> cir
    ) {
        RecipeManager.CachedCheck<C, T> originalCheck = cir.getReturnValue();
        cir.setReturnValue(new RecipeManager.CachedCheck<>() {
            @Override
            public @NotNull Optional<T> getRecipeFor(@NotNull C container, @NotNull Level level) {
                Optional<T> recipeOpt = originalCheck.getRecipeFor(container, level);
                return recipeOpt.filter(recipe -> {
                    ItemStack result = recipe.getResultItem(level.registryAccess());
                    return !VisionUtils.isBanned(result);
                });
            }
        });
    }

    @Inject(method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private <C extends Container, T extends Recipe<C>> void vminus$filterBannedRecipe(
            RecipeType<T> type, C container, Level level, CallbackInfoReturnable<Optional<T>> cir
    ) {
        Map<ResourceLocation, T> recipeMap = ((RecipeManagerAccessor) this).vminus$getByType(type);

        for (T recipe : recipeMap.values()) {
            if (!recipe.matches(container, level))
                continue;

            ItemStack result = recipe.getResultItem(level.registryAccess());
            if (VisionUtils.isBanned(result)) {
                cir.setReturnValue(Optional.empty());
                return;
            }
        }
    }

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("TAIL"))
    private void vminus$filterBannedRecipes(Map<ResourceLocation, JsonElement> p_44037_, ResourceManager resourceManager, ProfilerFiller p_44039_, CallbackInfo ci) {
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



    @Inject(method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private <C extends Container, T extends Recipe<C>> void vminus$filterBannedRecipeById(
            RecipeType<T> type, C container, Level level, ResourceLocation id, CallbackInfoReturnable<Optional<Pair<ResourceLocation, T>>> cir
    ) {
        Map<ResourceLocation, T> recipeMap = ((RecipeManagerAccessor) this).vminus$getByType(type);

        if (id != null) {
            T recipe = recipeMap.get(id);
            if (recipe != null && recipe.matches(container, level)) {
                ItemStack result = recipe.getResultItem(level.registryAccess());
                if (!VisionUtils.isBanned(result)) {
                    cir.setReturnValue(Optional.of(Pair.of(id, recipe)));
                    return;
                }
            }
        }

        for (Map.Entry<ResourceLocation, T> entry : recipeMap.entrySet()) {
            T recipe = entry.getValue();
            if (!recipe.matches(container, level))
                continue;

            ItemStack result = recipe.getResultItem(level.registryAccess());
            if (!VisionUtils.isBanned(result)) {
                cir.setReturnValue(Optional.of(Pair.of(entry.getKey(), recipe)));
                return;
            }
        }

        cir.setReturnValue(Optional.empty());
    }
}
