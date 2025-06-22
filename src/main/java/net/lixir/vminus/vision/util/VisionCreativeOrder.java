package net.lixir.vminus.vision.util;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class VisionCreativeOrder {
    private @Nullable ItemStack itemStack;
    private final ItemStack targetItemStack;
    private final @NotNull Boolean before;

    public VisionCreativeOrder(@Nullable ItemStack itemStack, ItemStack targetItemStack, @NotNull Boolean before) {
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

    public @Nullable ItemStack getTargetItemStack() {
        return targetItemStack;
    }

    public @NotNull Boolean isBefore() {
        return before;
    }
}
