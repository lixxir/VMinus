package net.lixir.vminus.visions.util;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public record VisionEntityVariant(@Nullable ResourceLocation name, @Nullable ResourceLocation texture, Integer weight, Boolean replace) {
}
