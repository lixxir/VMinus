package net.lixir.vminus.mixins.crafting;

import net.lixir.vminus.vision.util.VisionUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(CompoundIngredient.class)
public abstract class CompoundIngredientMixin {
    @Shadow
    private List<Ingredient> children;

    @Inject(method = "getItems", at = @At("HEAD"), cancellable = true)
    private void detour$replaceVisionItems(CallbackInfoReturnable<ItemStack[]> cir) {
        List<ItemStack> replacedItems = new ArrayList<>();
        for (Ingredient child : children) {
            for (ItemStack stack : child.getItems()) {
                VisionUtils.filterIngredient(stack, replacedItems);
            }
        }
        ItemStack[] filtered = replacedItems.toArray(new ItemStack[0]);
        if (cir.getReturnValue() != filtered)
            cir.setReturnValue(filtered);
    }
}
