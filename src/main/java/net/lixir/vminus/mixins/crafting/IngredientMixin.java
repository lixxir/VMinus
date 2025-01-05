package net.lixir.vminus.mixins.crafting;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionPropertyHandler;
import net.lixir.vminus.visions.util.VisionValueHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(Ingredient.class)
public abstract class IngredientMixin {
    @Shadow
    @Final
    private Ingredient.Value[] values;

    @Shadow public abstract boolean isEmpty();


    @Inject(method = "test*", at = @At("HEAD"), cancellable = true)
    public void vminus$test(@Nullable ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack == null) {
            cir.setReturnValue(false);
            return;
        }

        if (this.isEmpty()) {
            cir.setReturnValue(itemStack.isEmpty());
            return;
        }
        cir.setReturnValue(VisionPropertyHandler.matchesIngredient(itemStack, values));
    }


    @Inject(method = "getItems", at = @At("HEAD"), cancellable = true)
    public void vminus$getItems(CallbackInfoReturnable<ItemStack[]> cir) {
        List<ItemStack> replacedItems = new ArrayList<>();

        for (Ingredient.Value value : this.values) {
            for (ItemStack stack : value.getItems()) {
                JsonObject visionData = VisionHandler.getVisionData(stack);
                ItemStack replacementStack = VisionPropertyHandler.getIngredientReplacement(stack, visionData);

                if (replacementStack != null) {
                    replacedItems.add(replacementStack);
                } else if (!VisionPropertyHandler.isBanned(stack, visionData)) {
                    replacedItems.add(stack);
                }
            }
        }

        cir.setReturnValue(replacedItems.toArray(new ItemStack[0]));
    }
}
