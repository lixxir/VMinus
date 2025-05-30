package net.lixir.vminus.mixins.crafting;

import net.lixir.vminus.visions.ItemVision;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
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

import javax.annotation.Nullable;
import java.util.*;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(method = "createCheck", at = @At("RETURN"), cancellable = true)
    private static <C extends Container, T extends Recipe<C>> void vminus$filterBannedRecipes(RecipeType<T> p_220268_, CallbackInfoReturnable<RecipeManager.CachedCheck<C, T>> cir) {
        RecipeManager.CachedCheck<C, T> originalCheck = cir.getReturnValue();
        cir.setReturnValue(new RecipeManager.CachedCheck<>() {
            @Nullable
            private ResourceLocation lastRecipe;

            @Override
            public @NotNull Optional<T> getRecipeFor(@NotNull C p_220278_, @NotNull Level p_220279_) {
                Optional<T> recipeOpt = originalCheck.getRecipeFor(p_220278_, p_220279_);

                return recipeOpt.filter(recipe -> {
                    ItemStack result = recipe.getResultItem(p_220279_.registryAccess());
                    Boolean banned = ItemVision.of(result).ban.value(new VisionConditionArguments(result));
                    return banned == null || !banned;
                });
            }
        });
    }


    @Inject(method = "getRecipeFor*", at = @At("RETURN"), cancellable = true)
    private <C extends Container, T extends Recipe<C>> void vminus$filterBannedRecipe(RecipeType<T> type, C container, Level level, CallbackInfoReturnable<Optional<T>> cir) {
        Optional<T> recipeOpt = cir.getReturnValue();

        recipeOpt.ifPresent(recipe -> {
            ItemStack result = recipe.getResultItem(level.registryAccess());
            Boolean banned = ItemVision.of(result).ban.value(new VisionConditionArguments(result));

            if (banned != null && banned) {
                cir.setReturnValue(Optional.empty());
            }
        });
    }

    @Inject(method = "getRecipesFor", at = @At("RETURN"), cancellable = true)
    private <C extends Container, T extends Recipe<C>> void vminus$filterBannedRecipes(RecipeType<T> p_44057_, C p_44058_, Level level, CallbackInfoReturnable<List<T>> cir) {
        List<T> original = cir.getReturnValue();

        List<T> filtered = original.stream()
                .filter(recipe -> {
                    ItemStack result = recipe.getResultItem(level.registryAccess());
                    Boolean banned = ItemVision.of(result).ban.value(new VisionConditionArguments(result));
                    return banned == null || !banned;
                })
                .toList();

        cir.setReturnValue(new ArrayList<>(filtered));
    }



}
