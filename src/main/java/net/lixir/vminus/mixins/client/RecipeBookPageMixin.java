package net.lixir.vminus.mixins.client;

import net.minecraft.client.gui.screens.recipebook.RecipeBookPage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RecipeBookPage.class)
public abstract class RecipeBookPageMixin {
    @Unique
    private final RecipeBookPage vminus$recipeBookPage = (RecipeBookPage) (Object) this;


    /*
    @Inject(method = "updateCollections", at = @At("HEAD"), cancellable = true)
    private void vminus$filterBannedCollections(List<RecipeCollection> collections, boolean resetPage, CallbackInfo ci) {
        List<RecipeCollection> filtered = collections.stream()
                .filter(collection -> collection.getRecipes().stream()
                        .anyMatch(recipe -> {
                            ItemStack result = recipe.getResultItem(collection.registryAccess());
                            Boolean banned = ItemVision.of(result).ban.value(new VisionContext(result));
                            return banned == null || !banned;
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


     */

}
