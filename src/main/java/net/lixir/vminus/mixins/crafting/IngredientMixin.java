package net.lixir.vminus.mixins.crafting;

import net.lixir.vminus.core.conditions.VisionConditionArguments;
import net.lixir.vminus.core.util.VisionUtil;
import net.lixir.vminus.core.visions.ItemVision;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mixin(Ingredient.class)
public abstract class IngredientMixin {
    @Unique
    private final Ingredient vminus$ingredient = (Ingredient) (Object) this;


    @Inject(method = "test*", at = @At("HEAD"), cancellable = true)
    public void vminus$test(@Nullable ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        IngredientAccessor accessor = (IngredientAccessor) vminus$ingredient;
        if (itemStack == null) {
            cir.setReturnValue(false);
            return;
        }

        if (accessor.invokeIsEmpty()) {
            cir.setReturnValue(itemStack.isEmpty());
            return;
        }
        cir.setReturnValue(VisionUtil.matchesIngredient(itemStack, accessor.getValues()));
    }




    @Inject(method = "getItems", at = @At("HEAD"), cancellable = true)
    public void vminus$getItems(CallbackInfoReturnable<ItemStack[]> cir) {
        List<ItemStack> replacedItems = new ArrayList<>();
        IngredientAccessor accessor = (IngredientAccessor) vminus$ingredient;
        boolean changed = false;
        for (Ingredient.Value value : accessor.getValues()) {
            for (ItemStack stack : value.getItems()) {
                ItemStack replacementStack = ItemVision.getVision(stack).replace.value(new VisionConditionArguments(stack));
                Boolean banned = ItemVision.getVision(stack).ban.value(new VisionConditionArguments(stack));
                if (replacementStack != null) {
                    changed = true;
                    replacedItems.add(replacementStack);
                } else if ((banned == null || !banned)) {
                    changed = true;
                    replacedItems.add(stack);
                }
            }
        }
        if (changed)
            cir.setReturnValue(replacedItems.toArray(new ItemStack[0]));
    }
}
