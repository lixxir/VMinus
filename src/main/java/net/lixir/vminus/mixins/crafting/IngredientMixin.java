package net.lixir.vminus.mixins.crafting;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.ItemReplacement;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
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
        cir.setReturnValue(vMinus$matchesIngredient(itemStack, accessor.getValues()));
    }


    @Inject(method = "getItems", at = @At("HEAD"), cancellable = true)
    public void vminus$getItems(CallbackInfoReturnable<ItemStack[]> cir) {
        List<ItemStack> replacedItems = new ArrayList<>();
        IngredientAccessor accessor = (IngredientAccessor) vminus$ingredient;
        for (Ingredient.Value value : accessor.getValues()) {
            for (ItemStack stack : value.getItems()) {
                VisionUtils.filterIngredient(stack, replacedItems);
            }
        }
        ItemStack[] filtered = replacedItems.toArray(new ItemStack[0]);
        if (cir.getReturnValue() != filtered)
            cir.setReturnValue(filtered);
    }
    @Unique
    private static boolean vMinus$matchesIngredient(ItemStack targetStack, Ingredient.Value @NotNull [] values) {
        for (Ingredient.Value value : values) {
            for (ItemStack itemStack : value.getItems()) {
                VisionContext context = new VisionContext(itemStack);
                ItemReplacement itemReplacement = Vision.get(itemStack).getValue(VisionProperties.Items.REPLACE, context);

                if (itemReplacement != null) {
                    ItemStack replacementStack = itemReplacement.itemStack();
                    TagKey<Item> replacementTag = itemReplacement.tag();

                    if (replacementStack != null && ItemStack.isSameItem(replacementStack, targetStack)) {
                        return true;
                    }

                    if (replacementTag != null && targetStack.is(replacementTag)) {
                        return true;
                    }
                }

                Boolean banned = Vision.get(itemStack).getValue(VisionProperties.Items.BAN, context);
                if (Boolean.TRUE.equals(banned)) {
                    continue;
                }

                if (itemStack.is(targetStack.getItem())) {
                    return true;
                }
            }
        }
        return false;
    }

}
