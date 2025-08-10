package net.lixir.vminus.util;

import net.lixir.vminus.entity.attribute.VMinusAttributes;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class SizeAttributeUtil {
    public static float getWidth(@Nullable LivingEntity entity) {
        double width = 1;
        if (entity != null && entity.getAttributes().hasAttribute(VMinusAttributes.WIDTH))
            width *= entity.getAttributeValue(VMinusAttributes.WIDTH);
        return (float) Math.max(width, 0.25);
    }

    public static float getHeight(@Nullable LivingEntity entity) {
        double height = 1;
        if (entity != null && entity.getAttributes().hasAttribute(VMinusAttributes.HEIGHT))
            height *= entity.getAttributeValue(VMinusAttributes.HEIGHT);
        return (float) Math.max(height, 0.25);
    }
}
