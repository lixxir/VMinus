package net.lixir.vminus.vision;

import net.minecraft.resources.ResourceLocation;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

public interface VisionDuck {
    void vMinus$setVisionId(ResourceLocation id);

    @Nullable
    ResourceLocation vMinus$getVisionId();

    @NonNull
    VisionType<?> vMinus$getVisionType();

    default void vMinus$update(){}
}
