package net.lixir.vminus.mixins.client;

import net.minecraft.client.gui.screens.recipebook.RecipeButton;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(RecipeButton.class)
public abstract class RecipeButtonMixin {
    @Shadow protected abstract List<Recipe<?>> getOrderedRecipes();

    @Unique
    private final RecipeButton vminus$recipeButton = (RecipeButton) (Object) this;

    /*

    @Inject(method = "getOrderedRecipes", at = @At("RETURN"), cancellable = true)
    private void vminus$filterOrdered(CallbackInfoReturnable<List<Recipe<?>>> cir) {
        List<Recipe<?>> filtered = cir.getReturnValue().stream()
                .filter(recipe -> {
                    ItemStack result = recipe.getResultItem(vminus$recipeButton.getCollection().registryAccess());
                    Boolean banned = ItemVision.of(result).ban.value(new VisionContext(result));
                    return banned == null || !banned;
                })
                .toList();

        cir.setReturnValue(new ArrayList<>(filtered));
    }

     */


}
