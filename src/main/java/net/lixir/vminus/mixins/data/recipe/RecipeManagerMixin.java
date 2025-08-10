package net.lixir.vminus.mixins.data.recipe;

import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

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
                    Boolean banned = Vision.get(result).getValue(VisionProperties.Items.BAN, new VisionContext(result));
                    return !Boolean.TRUE.equals(banned);
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
            if (!recipe.matches(container, level)) continue;

            ItemStack result = recipe.getResultItem(level.registryAccess());
            Boolean banned = Vision.get(result).getValue(VisionProperties.Items.BAN, new VisionContext(result));

            if (!Boolean.TRUE.equals(banned)) {
                cir.setReturnValue(Optional.of(recipe));
                return;
            }
        }

        cir.setReturnValue(Optional.empty());
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
                Boolean banned = Vision.get(result).getValue(VisionProperties.Items.BAN, new VisionContext(result));

                if (!Boolean.TRUE.equals(banned)) {
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
            Boolean banned = Vision.get(result).getValue(VisionProperties.Items.BAN, new VisionContext(result));

            if (!Boolean.TRUE.equals(banned)) {
                cir.setReturnValue(Optional.of(Pair.of(entry.getKey(), recipe)));
                return;
            }
        }

        cir.setReturnValue(Optional.empty());
    }
}
