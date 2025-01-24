package net.lixir.vminus.mixins.crafting;

import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Ingredient.class)
public interface IngredientAccessor {
    @Accessor("values")
    Ingredient.Value[] getValues();

    @Invoker("isEmpty")
    boolean invokeIsEmpty();
}