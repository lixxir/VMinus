package net.lixir.vminus.core.util;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class VisionCreativeOrder {
    private @Nullable ItemStack itemStack;
    private final ItemStack targetItemStack;
    private final Boolean before;

    public VisionCreativeOrder(@Nullable ItemStack itemStack, ItemStack targetItemStack, Boolean before) {
        this.itemStack = itemStack;
        this.targetItemStack = targetItemStack;
        this.before = before;
    }

    public @Nullable ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(@Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getTargetItemStack() {
        return targetItemStack;
    }

    public Boolean isBefore() {
        return before;
    }
}
