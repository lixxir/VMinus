package net.lixir.vminus.util;

import net.lixir.vminus.entity.attribute.VMinusAttributes;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AttributeHelper {
    public static float applyProtection(float damage, LivingEntity entity, DamageSource source, Attribute attribute, TagKey<DamageType> tag) {
        if (source.is(tag)) {
            float specific = (float) entity.getAttributeValue(attribute) / 100f;
            damage -= damage * specific;
        }
        float generic = (float) entity.getAttributeValue(VMinusAttributes.PROTECTION) / 100f;
        damage -= damage * generic;
        return Math.max(damage, 0);
    }
}
