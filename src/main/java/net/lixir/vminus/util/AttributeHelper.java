package net.lixir.vminus.util;

import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber
public class AttributeHelper {

    public static float applyProtection(float damage, LivingEntity entity, DamageSource source, Attribute attribute, ResourceLocation resourceLocation) {
        if (source.is(TagKey.create(Registries.DAMAGE_TYPE, resourceLocation))) {
            float protectionPercentage = getAttributesFromArmor(entity, attribute);
            damage = damage - (damage * protectionPercentage);
        }
        float genericProtection = getAttributesFromArmor(entity, VMinusAttributes.PROTECTION.get());
        damage = damage - (damage * genericProtection);
        return Math.max(damage, 0);
    }

    public static float calculateHealthBoostFromItem(ItemStack itemStack, LivingEntity entity) {
        float healthBoost = 0.0f;
        float healthDifference = entity.getMaxHealth() - entity.getHealth();
        healthBoost += healthDifference * getAttributeFromItem(itemStack, VMinusAttributes.HEALTH_LOST_STAT_BOOST.get());
        return healthBoost;
    }

    public static float getAttributesFromArmor(Entity entity, Attribute attribute) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return 0;
        }
        float prot = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor()) {
                ItemStack itemStack = livingEntity.getItemBySlot(slot);
                float protection = getAttributeFromItem(itemStack, attribute);
                prot += protection;
            }
        }
        return Math.max(Math.min(prot, 1.0f), 0);
    }

    public static float getAttributeFromItem(ItemStack itemStack, Attribute attribute) {
        if (itemStack.isEmpty())
            return 0.0f;


        float total = 0.0f;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Collection<AttributeModifier> modifiers = itemStack.getAttributeModifiers(slot).get(attribute);

            if (modifiers.isEmpty()) {
                continue;
            }
            for (AttributeModifier modifier : modifiers) {
                total += modifier.getAmount();
            }
        }
        return total;
    }


}
