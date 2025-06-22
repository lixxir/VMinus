package net.lixir.vminus.mixins.crafting;

import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Ingredient.class)
public abstract class IngredientMixin {
    @Unique
    private final Ingredient vminus$ingredient = (Ingredient) (Object) this;

    /*
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

     */


/*
    @Inject(method = "getItems", at = @At("HEAD"), cancellable = true)
    public void vminus$getItems(CallbackInfoReturnable<ItemStack[]> cir) {
        List<ItemStack> replacedItems = new ArrayList<>();
        IngredientAccessor accessor = (IngredientAccessor) vminus$ingredient;
        boolean changed = false;
        for (Ingredient.Value value : accessor.getValues()) {
            for (ItemStack stack : value.getItems()) {
                VisionItemReplacement visionItemReplacement = ItemVision.of(stack).replace.value(new VisionContext(stack));
                if (visionItemReplacement == null)
                    continue;
                TagKey<Item> tagKey = visionItemReplacement.tag();
                ItemStack replacementStack = visionItemReplacement.itemStack();
                Boolean banned = ItemVision.of(stack).ban.value(new VisionContext(stack));
                if (tagKey != null ) {
                    var tagCollection = ForgeRegistries.ITEMS.tags();
                    if (tagCollection == null)
                        continue;
                    ITag<Item> tag = tagCollection.getTag(tagKey);
                    if (tag.isEmpty())
                        continue;
                    List<Item> itemList = tag.stream().toList();
                    for (Item item : itemList) {
                        changed = true;
                        replacedItems.add(item.getDefaultInstance());
                    }
                } else if (replacementStack != null && !replacementStack.isEmpty()) {
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

 */
}
