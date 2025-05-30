package net.lixir.vminus.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lixir.vminus.visions.ItemVision;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.recipebook.RecipeButton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(RecipeButton.class)
public abstract class RecipeButtonMixin {
    @Shadow protected abstract List<Recipe<?>> getOrderedRecipes();

    @Unique
    private final RecipeButton vminus$recipeButton = (RecipeButton) (Object) this;


    @Inject(method = "getOrderedRecipes", at = @At("RETURN"), cancellable = true)
    private void vminus$filterOrdered(CallbackInfoReturnable<List<Recipe<?>>> cir) {
        List<Recipe<?>> filtered = cir.getReturnValue().stream()
                .filter(recipe -> {
                    ItemStack result = recipe.getResultItem(vminus$recipeButton.getCollection().registryAccess());
                    Boolean banned = ItemVision.of(result).ban.value(new VisionConditionArguments(result));
                    return banned == null || !banned;
                })
                .toList();

        cir.setReturnValue(new ArrayList<>(filtered));
    }


}
