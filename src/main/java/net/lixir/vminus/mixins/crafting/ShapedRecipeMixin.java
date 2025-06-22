package net.lixir.vminus.mixins.crafting;

import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {

    /*
    @Inject(method = "getResultItem", at = @At("RETURN"), cancellable = true)
    public void vminus$getResultItem(RegistryAccess p_267111_, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack = cir.getReturnValue();
        if (itemStack == null)
            return;
        VisionItemReplacement visionItemReplacement = ItemVision.of(itemStack).replace.value(new VisionContext(itemStack));
        if (visionItemReplacement == null)
            return;
        ItemStack replacementStack = visionItemReplacement.itemStack();
        if (replacementStack != null) cir.setReturnValue(replacementStack);
    }

     */
}
