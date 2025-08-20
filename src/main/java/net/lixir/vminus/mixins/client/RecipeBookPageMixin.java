package net.lixir.vminus.mixins.client;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.client.gui.screens.recipebook.RecipeBookPage;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RecipeBookPage.class)
public abstract class RecipeBookPageMixin {
    @Unique
    private final RecipeBookPage vminus$recipeBookPage = (RecipeBookPage) (Object) this;



    @Inject(method = "updateCollections", at = @At("HEAD"), cancellable = true)
    private void vminus$filterBannedCollections(@NotNull List<RecipeCollection> collections, boolean resetPage, CallbackInfo ci) {
        List<RecipeCollection> filtered = collections.stream()
                .filter(collection -> collection.getRecipes().stream()
                        .anyMatch(recipe -> {
                            ItemStack result = recipe.getResultItem(collection.registryAccess());
                            return !VisionUtils.isBanned(result);
                        }))
                .toList();

        RecipeBookPageAccessor accessor = (RecipeBookPageAccessor) vminus$recipeBookPage;
        accessor.setRecipeCollections(filtered);
        accessor.setTotalPages((int) Math.ceil(filtered.size() / 20.0));
        if (accessor.getTotalPages() <= accessor.getCurrentPage() || resetPage) {
            accessor.setCurrentPage(0);
        }

        accessor.invokeUpdateButtonsForPage();

        ci.cancel();
    }
}
