package net.lixir.vminus.mixins.crafting;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.ItemReplacement;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapelessRecipe.class)
public class ShapelessRecipeMixin {
    @Inject(method = "getResultItem", at = @At("RETURN"), cancellable = true)
    public void vminus$getResultItem(RegistryAccess registryAccess, @NotNull CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack = cir.getReturnValue();
        if (itemStack == null)
            return;
        ItemReplacement itemReplacement = Vision.get(itemStack).getValue(VisionProperties.Items.REPLACE, new VisionContext(itemStack));
        if (itemReplacement == null)
            return;
        ItemStack replacementStack = itemReplacement.itemStack();
        if (replacementStack != null)
            cir.setReturnValue(replacementStack);
    }
}
