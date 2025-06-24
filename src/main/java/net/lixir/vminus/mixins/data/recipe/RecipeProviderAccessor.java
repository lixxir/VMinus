package net.lixir.vminus.mixins.data.recipe;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RecipeProvider.class)
public interface RecipeProviderAccessor {
    @Invoker("has")
    static InventoryChangeTrigger.TriggerInstance has(ItemLike p_125978_) {
       throw new AssertionError();
    }
}
