package net.lixir.vminus.mixins.data.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleCookingRecipeBuilder.class)
public abstract class SimpleCookingRecipeBuilderMixin {

    @Shadow @Final private Advancement.Builder advancement;

    // Adds a fallback if a criterion is not specified.
    @Inject(method = "save", at = @At("HEAD"))
    private void vminus$save(CallbackInfo ci) {
        advancement.addCriterion("ignore", RecipeProviderAccessor.has(Items.AIR));
    }

}
