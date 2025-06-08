package net.lixir.vminus.mixins;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.registry.VMinusRegistries;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShapelessRecipeBuilder.class)
public abstract class ShapelessRecipeBuilderMixin {

    @Shadow @Final private Advancement.Builder advancement;

    // Adds a fallback if a criterion is not specified.
    @Inject(method = "save", at = @At("HEAD"))
    private void vminus$save(CallbackInfo ci) {
        advancement.addCriterion("ignore", RecipeProviderAccessor.has(Items.AIR));
    }

}
