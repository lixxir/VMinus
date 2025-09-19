package net.lixir.vminus.vision.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CreativeOrder {
    private @Nullable ItemStack itemStack;
    private final ItemStack targetItemStack;
    private final @NotNull Boolean before;
    private final TagKey<Item> tagKey;

    @Contract("_, _ -> new")
    public static @NotNull CreativeOrder before(@NotNull Item item, @NotNull Item target) {
        return new CreativeOrder(item.getDefaultInstance(), target.getDefaultInstance(), true, null);
    }

    @Contract("_, _ -> new")
    public static @NotNull CreativeOrder after(@NotNull Item item, @NotNull Item target) {
        return new CreativeOrder(item.getDefaultInstance(), target.getDefaultInstance(), false, null);
    }

    @Contract("_, _ -> new")
    public static @NotNull CreativeOrder tagBefore(TagKey<Item> tag, @NotNull Item target) {
        return new CreativeOrder(null, target.getDefaultInstance(), true, tag);
    }

    @Contract("_, _ -> new")
    public static @NotNull CreativeOrder tagAfter(TagKey<Item> tag, @NotNull Item target) {
        return new CreativeOrder(null, target.getDefaultInstance(), false, tag);
    }

    @Contract("_, _ -> new")
    public static @NotNull CreativeOrder beforeTag(@NotNull Item item, TagKey<Item> tag) {
        return new CreativeOrder(item.getDefaultInstance(), null, true, tag);
    }

    @Contract("_, _ -> new")
    public static @NotNull CreativeOrder afterTag(@NotNull Item item, TagKey<Item> tag) {
        return new CreativeOrder(item.getDefaultInstance(), null, false, tag);
    }

    public CreativeOrder(@Nullable ItemStack itemStack, @Nullable ItemStack targetItemStack, @NotNull Boolean before, TagKey<Item> tagKey) {
        this.itemStack = itemStack;
        if (itemStack != null)
            itemStack.setCount(1);
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
