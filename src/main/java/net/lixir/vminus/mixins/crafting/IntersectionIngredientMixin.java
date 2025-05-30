package net.lixir.vminus.mixins.crafting;

import net.lixir.vminus.visions.ItemVision;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.util.VisionItemReplacement;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IntersectionIngredient;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(IntersectionIngredient.class)
public abstract class IntersectionIngredientMixin {

    @Shadow private ItemStack[] intersectedMatchingStacks;
    @Shadow @Final private List<Ingredient> children;


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

            VisionItemReplacement replacement = ItemVision.of(original).replace.value(new VisionConditionArguments(original));
            Boolean banned = ItemVision.of(original).ban.value(new VisionConditionArguments(original));

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


}
