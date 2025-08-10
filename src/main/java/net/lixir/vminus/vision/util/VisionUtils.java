package net.lixir.vminus.vision.util;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperty;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

public class VisionUtils {
    public static void filterRecipeList(RecipeCollection recipeCollection, @NotNull CallbackInfoReturnable<List<Recipe<?>>> cir) {
        List<Recipe<?>> filtered = List.copyOf(cir.getReturnValue()).stream()
                .filter(r -> {
                    ItemStack result = r.getResultItem(recipeCollection.registryAccess());
                    Boolean ban = Vision.get(result).getValue(VisionProperties.Items.BAN, new VisionContext(result));
                    return ban == null || !ban;
                })
                .toList();
        if (!cir.getReturnValue().equals(filtered))
            cir.setReturnValue(new ArrayList<>(filtered));
    }

    public static <T> @Nullable T getOverrideValue(VisionDuck visionDuck, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        Vision vision = Vision.get(visionDuck);
        return vision.getValue(visionProperty, visionContext);
    }

    public static <T> T tryOverride(CallbackInfoReturnable<T> cir, VisionDuck visionDuck, VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        T value = getOverrideValue(visionDuck, visionProperty, visionContext);
        if (value != null) {
            cir.setReturnValue(value);
        }
        return value;
    }

    public static boolean isItemBannedOrReplaced(ItemStack stack, ItemReplacement replacement) {
        Boolean ban = Vision.getValue(stack, VisionProperties.Items.BAN);
        return isItemBannedOrReplaced(replacement, ban);
    }

    public static boolean isItemBannedOrReplaced(ItemStack stack, Boolean banned) {
        ItemReplacement replacement = ItemReplacement.from(stack);
        return isItemBannedOrReplaced(replacement, banned);
    }

    public static boolean isItemBannedOrReplaced(ItemStack stack) {
        ItemReplacement replacement = ItemReplacement.from(stack);
        Boolean ban = Vision.getValue(stack, VisionProperties.Items.BAN);
        return isItemBannedOrReplaced(replacement, ban);
    }

    public static boolean isItemBannedOrReplaced(ItemReplacement replacement, Boolean banned) {
        return (replacement != null && (replacement.itemStack() != null || replacement.tag() != null)) || Boolean.TRUE.equals(banned);
    }

    public static <T> T tryCancel(CallbackInfo ci, VisionDuck visionDuck, VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        T value = getOverrideValue(visionDuck, visionProperty, visionContext);
        if (value != null) {
            ci.cancel();
        }
        return value;
    }

    public static void filterIngredient(ItemStack stack, List<ItemStack> replacedItems) {
        ItemReplacement replacement = ItemReplacement.from(stack);
        if (replacement != null) {
            TagKey<Item> tagKey = replacement.tag();
            ItemStack replacementStack = replacement.itemStack();

            if (tagKey != null) {
                var tagCollection = ForgeRegistries.ITEMS.tags();
                if (tagCollection == null)
                    return;

                ITag<Item> tag = tagCollection.getTag(tagKey);
                if (!tag.isEmpty()) {
                    for (Item item : tag)
                        replacedItems.add(item.getDefaultInstance());
                    return;
                }
            } else if (replacementStack != null && !replacementStack.isEmpty()) {
                replacedItems.add(replacementStack);
                return;
            }
        }
        if (isItemBannedOrReplaced(stack, replacement))
            return;

        replacedItems.add(stack);
    }

}

