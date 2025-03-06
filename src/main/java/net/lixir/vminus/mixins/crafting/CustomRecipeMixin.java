package net.lixir.vminus.mixins.crafting;

import net.lixir.vminus.core.conditions.VisionConditionArguments;
import net.lixir.vminus.core.visions.ItemVision;
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
        ItemStack itemStack = ItemVision.getVision(cir.getReturnValue()).replace.value(new VisionConditionArguments(cir.getReturnValue()));
        if (itemStack != null) cir.setReturnValue(itemStack);
    }
}
