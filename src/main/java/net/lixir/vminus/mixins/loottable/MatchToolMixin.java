package net.lixir.vminus.mixins.loottable;

import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MatchTool.class)
public class MatchToolMixin {

    /*
    @Inject(method = "test*", at = @At("RETURN"), cancellable = true, remap = false)
    private void test(LootContext context, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return;

        ItemStack stack = context.getParamOrNull(LootContextParams.TOOL);
        if (stack == null)
            return;

        MatchTool self = (MatchTool) (Object) this;
        if (self == null)
            return;
        ItemPredicate predicate = ((MatchToolAccessor) self).getPredicate();
        if (predicate == null)
            return;

        Set<Item> items = ((ItemPredicateAccessor) predicate).getItems();
        if (items == null)
            return;
        for (Item item : items) {
            ItemStack predicateStack = new ItemStack(item);
            VisionContext args = new VisionContext(predicateStack);
            VisionItemReplacement visionItemReplacement = ItemVision.of(predicateStack).replace.value(args);

            if (visionItemReplacement != null) {
                ItemStack replacementStack = visionItemReplacement.itemStack();
                TagKey<Item> tag = visionItemReplacement.tag();

                if (tag != null && stack.is(tag)) {
                    cir.setReturnValue(true);
                    return;
                }

                if (replacementStack != null && predicate.matches(replacementStack)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }

     */
}
