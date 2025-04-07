package net.lixir.vminus.mixins.crafting;

import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.util.VisionItemReplacement;
import net.lixir.vminus.visions.ItemVision;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {

    @Inject(method = "getResultItem", at = @At("RETURN"), cancellable = true)
    public void vminus$getResultItem(RegistryAccess p_267111_, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack = cir.getReturnValue();
        if (itemStack == null)
            return;
        VisionItemReplacement visionItemReplacement = ItemVision.of(itemStack).replace.value(new VisionConditionArguments(itemStack));
        if (visionItemReplacement == null)
            return;
        ItemStack replacementStack = visionItemReplacement.itemStack();
        if (replacementStack != null) cir.setReturnValue(replacementStack);
    }
}
