package net.lixir.vminus.mixins.crafting;

import net.lixir.vminus.vision.util.ItemReplacement;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapelessRecipe.class)
public class ShapelessRecipeMixin {
    @Inject(method = "getResultItem", at = @At("RETURN"), cancellable = true)
    private void vMinus$getResultItem(RegistryAccess registryAccess, @NotNull CallbackInfoReturnable<ItemStack> cir) {
        ItemReplacement.tryReplace(cir.getReturnValue(), cir::setReturnValue);
    }
}