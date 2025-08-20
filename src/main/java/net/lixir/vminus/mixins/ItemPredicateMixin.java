package net.lixir.vminus.mixins;

import net.lixir.vminus.vision.util.ItemReplacement;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

@Mixin(ItemPredicate.class)
public abstract class ItemPredicateMixin {
    @Shadow public abstract boolean matches(ItemStack p_45050_);

    @Redirect(
            method = "matches(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"
            )
    )
    private boolean vMinus$replaceItemCheck(Set items, Object object) {
        if (!(object instanceof Item item))
            return items.contains(object);

        ItemStack resolved = ItemReplacement.resolve(item.getDefaultInstance());
        return items.contains(item) || (!resolved.isEmpty() && items.contains(resolved.getItem()));
    }

    @Inject(
        method = "matches(Lnet/minecraft/world/item/ItemStack;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vision$injectReplacements(ItemStack original, CallbackInfoReturnable<Boolean> cir) {
        ItemStack resolved = ItemReplacement.resolve(original);

        if (!resolved.isEmpty()) {
            cir.setReturnValue(matches(resolved));
        }
    }
}
