package net.lixir.vminus.vision.util;

import net.lixir.vminus.resources.data.bans.BannedRecipeManager;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperty;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

public class VisionUtils {
    public static boolean isRecipeBanned(Recipe<?> recipe, ResourceLocation id, RegistryAccess access) {
        if (BannedRecipeManager.INSTANCE.isBanned(id))
            return true;

        ItemStack result = recipe.getResultItem(access);
        if (VisionUtils.isBanned(result.getItem()))
            return true;

        return recipe.getIngredients().stream()
                .flatMap(ing -> Arrays.stream(ing.getItems()))
                .map(ItemStack::getItem)
                .allMatch(VisionUtils::isBanned);
    }

    public static <T> @Nullable T getOverrideValue(VisionDuck visionDuck, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        Vision vision = Vision.get(visionDuck);
        return vision.getValue(visionProperty, visionContext);
    }

    public static <T> T tryOverride(T firstType, T otherType) {
        if (otherType != null) {
            return otherType;
        }
        return firstType;
    }

    public static <T> T tryOverride(T originalValue, VisionDuck visionDuck, VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        T value = getOverrideValue(visionDuck, visionProperty, visionContext);
        return value != null ? value : originalValue;
    }

    public static <T> T tryOverride(CallbackInfoReturnable<T> cir, VisionDuck visionDuck, VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        T value = getOverrideValue(visionDuck, visionProperty, visionContext);
        if (value != null) {
            cir.setReturnValue(value);
        }
        return value;
    }

    public static boolean isItemBannedOrReplaced(ItemStack stack, ItemReplacement replacement) {
        return isItemBannedOrReplaced(replacement, isBanned(stack));
    }

    public static boolean isItemBannedOrReplaced(ItemStack stack, boolean banned) {
        ItemReplacement replacement = ItemReplacement.from(stack);
        return isItemBannedOrReplaced(replacement, banned);
    }

    public static boolean isItemBannedOrReplaced(ItemStack stack) {
        ItemReplacement replacement = ItemReplacement.from(stack);
        return isItemBannedOrReplaced(replacement, isBanned(stack));
    }

    public static boolean isItemBannedOrReplaced(ItemReplacement replacement, boolean banned) {
        return (replacement != null && (replacement.itemStack() != null || replacement.tag() != null)) || banned;
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
            if (replacement.itemStack() != null) {
                replacedItems.add(replacement.itemStack());
                return;
            } else if (replacement.tag() != null) {
                ITag<Item> tag = ForgeRegistries.ITEMS.tags().getTag(replacement.tag());
                if (!tag.isEmpty()) {
                    for (Item item : tag) replacedItems.add(item.getDefaultInstance());
                    return;
                }
            }
        }

        if (!isItemBannedOrReplaced(stack, replacement)) {
            replacedItems.add(stack.copy());
        }
    }

    public static boolean isBanned(Item item) {
        Boolean ban = Vision.getValue(item, VisionProperties.Items.BAN);
        ItemReplacement replacement = Vision.getValue(item, VisionProperties.Items.REPLACE);
        return Boolean.TRUE.equals(ban) && ItemReplacement.resolve(replacement).isEmpty();
    }

    public static boolean isBanned(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty())
            return false;
        Boolean ban = Vision.getValue(stack, VisionProperties.Items.BAN);
        ItemReplacement replacement = Vision.getValue(stack, VisionProperties.Items.REPLACE);
        return Boolean.TRUE.equals(ban) && ItemReplacement.resolve(replacement).isEmpty();
    }
}

