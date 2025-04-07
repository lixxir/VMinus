package net.lixir.vminus.visions.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public record VisionItemReplacement(@Nullable ItemStack itemStack, @Nullable TagKey<Item> tag) {}
