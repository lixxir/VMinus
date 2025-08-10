package net.lixir.vminus.mixins.crafting;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(RecipeCollection.class)
public class RecipeCollectionMixin {
    @Shadow
    @Final
    private Set<Recipe<?>> craftable;

    @Unique
    private final RecipeCollection vminus$recipeCollection = (RecipeCollection) (Object) this;


    @Inject(method = "isCraftable", at = @At("RETURN"), cancellable = true)
    private void vMinus$isCraftable(@NotNull Recipe<?> recipe, CallbackInfoReturnable<Boolean> cir) {
        ItemStack result = recipe.getResultItem(vminus$recipeCollection.registryAccess());
        if (result.isEmpty())
            return;
        Boolean ban = Vision.get(result).getValue(VisionProperties.Items.BAN, new VisionContext(result));
        if (ban != null && ban) {
            cir.setReturnValue(false);
        }
    }


    @Inject(method = "canCraft", at = @At("RETURN"))
    private void vminus$filterBannedRecipes(StackedContents contents, int width, int height, RecipeBook book, CallbackInfo ci) {
        Set<Recipe<?>> filtered = craftable.stream()
                .filter(recipe -> {
                    ItemStack result = recipe.getResultItem(vminus$recipeCollection.registryAccess());
                    Boolean ban = Vision.get(result).getValue(VisionProperties.Items.BAN, new VisionContext(result));
                    return ban == null || !ban;
                })
                .collect(Collectors.toSet());
        craftable.clear();
        craftable.addAll(filtered);
    }


    @Inject(method = "getRecipes(Z)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private void vminus$filterBannedRecipes(boolean onlyCraftable, CallbackInfoReturnable<List<Recipe<?>>> cir) {
        VisionUtils.filterRecipeList(vminus$recipeCollection, cir);
    }

    @Inject(method = "getDisplayRecipes(Z)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private void vminus$filterDisplayed(boolean onlyNotCraftable, CallbackInfoReturnable<List<Recipe<?>>> cir) {
        VisionUtils.filterRecipeList(vminus$recipeCollection, cir);
    }
}
