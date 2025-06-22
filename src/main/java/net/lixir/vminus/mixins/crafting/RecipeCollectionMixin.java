package net.lixir.vminus.mixins.crafting;

import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Set;

@Mixin(RecipeCollection.class)
public class RecipeCollectionMixin {
    @Shadow @Final private Set<Recipe<?>> craftable;

    @Unique private final RecipeCollection vminus$recipeCollection = (RecipeCollection) (Object) this;

    /*
    @Inject(method = "isCraftable", at = @At("RETURN"), cancellable = true)
    private void vminus$isCraftable(Recipe<?> recipe, CallbackInfoReturnable<Boolean> cir) {
        ItemStack result = recipe.getResultItem(vminus$recipeCollection.registryAccess());
        if (result.isEmpty())
            return;
        Boolean banned = ItemVision.of(result).ban.value(new VisionContext(result));
        if (banned != null && banned) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canCraft", at = @At("RETURN"))
    private void vminus$filterBannedRecipes(StackedContents contents, int width, int height, RecipeBook book, CallbackInfo ci) {
        craftable.removeIf(recipe -> {
            ItemStack result = recipe.getResultItem(vminus$recipeCollection.registryAccess());
            Boolean banned = ItemVision.of(result).ban.value(new VisionContext(result));
            return banned != null && banned;
        });
    }


    @Inject(method = "getRecipes(Z)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private void vminus$filterBannedRecipes(boolean onlyCraftable, CallbackInfoReturnable<List<Recipe<?>>> cir) {
        List<Recipe<?>> filtered = cir.getReturnValue().stream()
                .filter(r -> {
                    ItemStack result = r.getResultItem(vminus$recipeCollection.registryAccess());
                    Boolean banned = ItemVision.of(result).ban.value(new VisionContext(result));
                    return banned == null || !banned;
                })
                .toList();
        cir.setReturnValue(new ArrayList<>(filtered));
    }



    @Inject(method = "getDisplayRecipes(Z)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private void vminus$filterDisplayed(boolean onlyNotCraftable, CallbackInfoReturnable<List<Recipe<?>>> cir) {
        List<Recipe<?>> filtered = cir.getReturnValue().stream()
                .filter(r -> {
                    ItemStack result = r.getResultItem(vminus$recipeCollection.registryAccess());
                    Boolean banned = ItemVision.of(result).ban.value(new VisionContext(result));
                    return banned == null || !banned;
                })
                .toList();
        cir.setReturnValue(new ArrayList<>(filtered));
    }

     */




}
