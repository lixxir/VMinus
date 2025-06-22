package net.lixir.vminus.mixins.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.DifferenceIngredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(DifferenceIngredient.class)
public abstract class DifferenceIngredientMixin {
    @Shadow private ItemStack[] filteredMatchingStacks;

    @Shadow @Final private Ingredient base;

    @Shadow @Final private Ingredient subtracted;

    /*
    @Inject(method = "getItems", at = @At("HEAD"), cancellable = true)
    private void detour$replaceAndFilterVisionItems(CallbackInfoReturnable<ItemStack[]> cir) {
        if (this.filteredMatchingStacks != null)
            return;

        List<ItemStack> replacedItems = new ArrayList<>();
        for (ItemStack original : base.getItems()) {
            if (subtracted.test(original))
                continue;

            VisionItemReplacement replacement = ItemVision.of(original).replace.value(new VisionContext(original));
            Boolean banned = ItemVision.of(original).ban.value(new VisionContext(original));
            TagKey<Item> tagKey = replacement != null ? replacement.tag() : null;
            ItemStack replacementStack = replacement != null ? replacement.itemStack() : null;

            if (tagKey != null) {
                var tagCollection = ForgeRegistries.ITEMS.tags();
                if (tagCollection == null)
                    continue;

                ITag<Item> tag = tagCollection.getTag(tagKey);
                if (tag.isEmpty())
                    continue;

                for (Item item : tag) {
                    replacedItems.add(item.getDefaultInstance());
                }
            } else if (replacementStack != null && !replacementStack.isEmpty()) {
                replacedItems.add(replacementStack);
            } else if (banned == null || !banned) {
                replacedItems.add(original);
            }
        }

        this.filteredMatchingStacks = replacedItems.toArray(new ItemStack[0]);
        cir.setReturnValue(this.filteredMatchingStacks);
    }


     */

}
