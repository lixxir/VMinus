package net.lixir.vminus.mixins.crafting;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(CompoundIngredient.class)
public abstract class CompoundIngredientMixin {
    @Shadow
    private List<Ingredient> children;


    /*
    @Inject(method = "getItems", at = @At("HEAD"), cancellable = true)
    private void detour$replaceVisionItems(CallbackInfoReturnable<ItemStack[]> cir) {
        List<ItemStack> replacedItems = new ArrayList<>();
        boolean changed = false;

        for (Ingredient child : children) {
            for (ItemStack stack : child.getItems()) {
                VisionItemReplacement replacement = ItemVision.of(stack).replace.value(new VisionContext(stack));
                if (replacement == null) continue;

                TagKey<Item> tagKey = replacement.tag();
                ItemStack replacementStack = replacement.itemStack();
                Boolean banned = ItemVision.of(stack).ban.value(new VisionContext(stack));

                if (tagKey != null) {
                    var tagCollection = ForgeRegistries.ITEMS.tags();
                    if (tagCollection == null) continue;

                    ITag<Item> tag = tagCollection.getTag(tagKey);
                    if (tag.isEmpty()) continue;

                    for (Item item : tag) {
                        changed = true;
                        replacedItems.add(item.getDefaultInstance());
                    }
                } else if (replacementStack != null && !replacementStack.isEmpty()) {
                    changed = true;
                    replacedItems.add(replacementStack);
                } else if (banned == null || !banned) {
                    changed = true;
                    replacedItems.add(stack);
                }
            }
        }

        if (changed) {
            cir.setReturnValue(replacedItems.toArray(new ItemStack[0]));
        }
    }


     */

}
