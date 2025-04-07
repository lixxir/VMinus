package net.lixir.vminus.util;

import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class SizeAttributeUtil {
    public static float getWidth(@Nullable LivingEntity entity) {
        double width = 1;
        if (entity != null && entity.getAttributes().hasAttribute(VMinusAttributes.WIDTH.get()))
            width *= entity.getAttributeValue(VMinusAttributes.WIDTH.get());
        return (float) Math.max(width, 0.25);
    }

    public static float getHeight(@Nullable LivingEntity entity) {
        double height = 1;
        if (entity != null && entity.getAttributes().hasAttribute(VMinusAttributes.HEIGHT.get()))
            height *= entity.getAttributeValue(VMinusAttributes.HEIGHT.get());
        return (float) Math.max(height, 0.25);
    }
}
