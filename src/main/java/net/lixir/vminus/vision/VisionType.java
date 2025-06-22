package net.lixir.vminus.vision;

import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record VisionType(
        String id,
        String multiList,
        String directory,
        Class<?> classType,
        Function<ResourceLocation, ?> registryGetter,
        BiConsumer<Object, Integer> visionSetter
) {

    public void applyVision(Object target, int index) {
        visionSetter.accept(target, index);
    }
}
