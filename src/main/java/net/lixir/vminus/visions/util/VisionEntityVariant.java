package net.lixir.vminus.visions.util;

import net.minecraft.resources.ResourceLocation;

public record VisionEntityVariant(String name, ResourceLocation texture, Integer weight, Boolean replace) {
}
