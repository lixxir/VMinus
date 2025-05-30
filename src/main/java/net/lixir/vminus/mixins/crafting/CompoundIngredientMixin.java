package net.lixir.vminus.mixins.crafting;

import net.lixir.vminus.visions.ItemVision;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.util.VisionItemReplacement;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(CompoundIngredient.class)
public abstract class CompoundIngredientMixin {
    @Shadow private List<Ingredient> children;


    @Inject(method = "getItems", at = @At("HEAD"), cancellable = true)
    private void detour$replaceVisionItems(CallbackInfoReturnable<ItemStack[]> cir) {
        List<ItemStack> replacedItems = new ArrayList<>();
        boolean changed = false;

        for (Ingredient child : children) {
            for (ItemStack stack : child.getItems()) {
                VisionItemReplacement replacement = ItemVision.of(stack).replace.value(new VisionConditionArguments(stack));
                if (replacement == null) continue;

                TagKey<Item> tagKey = replacement.tag();
                ItemStack replacementStack = replacement.itemStack();
                Boolean banned = ItemVision.of(stack).ban.value(new VisionConditionArguments(stack));

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


}
