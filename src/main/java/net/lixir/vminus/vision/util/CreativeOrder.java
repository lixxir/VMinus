package net.lixir.vminus.vision.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CreativeOrder {
    private @Nullable ItemStack itemStack;
    private final ItemStack targetItemStack;
    private final @NotNull Boolean before;
    private final TagKey<Item> tagKey;

    public CreativeOrder(@Nullable ItemStack itemStack, @Nullable ItemStack targetItemStack, @NotNull Boolean before, TagKey<Item> tagKey) {
        this.itemStack = itemStack;
        this.targetItemStack = targetItemStack;
        this.before = before;
        this.tagKey = tagKey;
    }

    public @Nullable ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(@Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public @Nullable ItemStack getTargetItemStack() {
        return targetItemStack;
    }

    public @NotNull Boolean isBefore() {
        return before;
    }

    public TagKey<Item> getTagKey() {
        return tagKey;
    }
}
