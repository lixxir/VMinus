package net.lixir.vminus.mixins.crafting;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.ItemReplacement;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CustomRecipe.class)
public class CustomRecipeMixin {

    @Inject(method = "getResultItem", at = @At("RETURN"), cancellable = true)
    public void vminus$getResultItem(RegistryAccess p_267111_, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack = cir.getReturnValue();
        if (itemStack == null)
            return;
        ItemReplacement replacement = VisionUtils.getOverrideValue(((VisionDuck) itemStack.getItem()), VisionProperties.Items.REPLACE, new VisionContext(itemStack));
        if (replacement == null)
            return;
        ItemStack replacementStack = replacement.itemStack();
        if (replacementStack != null) cir.setReturnValue(replacementStack);
    }


}
