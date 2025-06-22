package net.lixir.vminus.mixins.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IntersectionIngredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(IntersectionIngredient.class)
public abstract class IntersectionIngredientMixin {
    @Shadow
    private ItemStack[] intersectedMatchingStacks;

    @Shadow
    @Final
    private List<Ingredient> children;


    /*
    @Inject(method = "getItems", at = @At("HEAD"), cancellable = true)
    private void detour$replaceAndIntersectVisionItems(CallbackInfoReturnable<ItemStack[]> cir) {
        if (this.intersectedMatchingStacks != null)
            return;

        List<ItemStack> result = new ArrayList<>();

        for (ItemStack original : children.get(0).getItems()) {
            boolean matchesAll = true;

            for (int i = 1; i < children.size(); i++) {
                if (!children.get(i).test(original)) {
                    matchesAll = false;
                    break;
                }
            }

            if (!matchesAll)
                continue;

            VisionItemReplacement replacement = ItemVision.of(original).replace.value(new VisionContext(original));
            Boolean banned = ItemVision.of(original).ban.value(new VisionContext(original));

            if (Boolean.TRUE.equals(banned))
                continue;

            if (replacement != null) {
                TagKey<Item> tag = replacement.tag();
                ItemStack replacementStack = replacement.itemStack();

                if (tag != null) {
                    var tagCollection = ForgeRegistries.ITEMS.tags();
                    if (tagCollection != null) {
                        ITag<Item> tagItems = tagCollection.getTag(tag);
                        if (!tagItems.isEmpty()) {
                            for (Item item : tagItems) {
                                result.add(item.getDefaultInstance());
                            }
                        }
                    }
                    continue;
                } else if (replacementStack != null && !replacementStack.isEmpty()) {
                    result.add(replacementStack);
                    continue;
                }
            }

            result.add(original);
        }

        this.intersectedMatchingStacks = result.toArray(new ItemStack[0]);
        cir.setReturnValue(this.intersectedMatchingStacks);
    }


     */

}
