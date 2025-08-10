package net.lixir.vminus.vision.util;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ItemStackWrapper(ItemStack itemStack) {
    @Override
    public @NotNull ItemStack itemStack() {
        return itemStack.copy();
    }
}
