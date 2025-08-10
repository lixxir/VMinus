package net.lixir.vminus.mixins.client;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.client.gui.screens.recipebook.RecipeButton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(RecipeButton.class)
public abstract class RecipeButtonMixin {
    @Unique
    private final RecipeButton vminus$recipeButton = (RecipeButton) (Object) this;


    @Inject(method = "getOrderedRecipes", at = @At("RETURN"), cancellable = true)
    private void vminus$filterOrdered(@NotNull CallbackInfoReturnable<List<Recipe<?>>> cir) {
        List<Recipe<?>> filtered = cir.getReturnValue().stream()
                .filter(recipe -> {
                    ItemStack result = recipe.getResultItem(vminus$recipeButton.getCollection().registryAccess());
                    Boolean ban = Vision.get(result).getValue(VisionProperties.Items.BAN, new VisionContext(result));
                    return ban == null || !ban;
                })
                .toList();

        cir.setReturnValue(new ArrayList<>(filtered));
    }
}
