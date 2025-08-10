package net.lixir.vminus.vision.util;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public record ItemReplacement(@Nullable ItemStack itemStack, @Nullable TagKey<Item> tag) {
    @Override
    public @Nullable ItemStack itemStack() {
        return itemStack != null ? itemStack.copy() : null;
    }

    public static @Nullable ItemReplacement from(Item item) {
        return from(item, new VisionContext(item));
    }

    public static @Nullable ItemReplacement from(ItemStack stack) {
        return from(stack, new VisionContext(stack));
    }

    public static @Nullable ItemReplacement from(Item item, @Nullable VisionContext visionContext) {
        return Vision.getValue(item, VisionProperties.Items.REPLACE, visionContext);
    }

    public static @Nullable ItemReplacement from(ItemStack stack, @Nullable VisionContext visionContext) {
        return Vision.getValue(stack, VisionProperties.Items.REPLACE, visionContext);
    }

    @SuppressWarnings("deprecation")
    public static ItemStack resolve(@Nullable ItemReplacement replacement) {
        if (replacement == null)
            return ItemStack.EMPTY;
        ItemStack itemStack = replacement.itemStack;
        TagKey<Item> tag = replacement.tag;
        if (itemStack != null && !itemStack.isEmpty()) {
            return itemStack;
        } else if (tag != null) {
            return BuiltInRegistries.ITEM
                    .getTag(tag)
                    .flatMap(tagContents -> tagContents.stream().findFirst())
                    .map(Holder::value)
                    .orElse(ItemStack.EMPTY.getItem()).getDefaultInstance();
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static boolean tryReplace(ItemStack original, Consumer<ItemStack> apply) {
        Boolean ban = Vision.getValue(original, VisionProperties.Items.BAN);
        ItemReplacement replacement = Vision.getValue(original, VisionProperties.Items.REPLACE);
        if (VisionUtils.isItemBannedOrReplaced(replacement, ban)) {
            ItemStack replaced = resolve(replacement);
            if (replaced != null) {
                replaced.setCount(original.getCount());
                apply.accept(replaced);
                return true;
            }
        }
        return false;
    }
}
