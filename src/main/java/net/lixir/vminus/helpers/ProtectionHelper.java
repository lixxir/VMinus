package net.lixir.vminus.helpers;
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
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;

@Mod.EventBusSubscriber
public class ProtectionHelper {

    public static float applyProtection(float damage, LivingEntity entity, DamageSource source, String protectionType) {
        if (source.isMagic()) {
            return applyMagicProtection(damage, entity, protectionType);
        } else if (source.isFall()) {
            return applyFallProtection(damage, entity, protectionType);
        } else if (source.isFire()) {
            return applyFireProtection(damage, entity, protectionType);
        } else if (isBluntDamage(source)) {
            return applyBluntProtection(damage, entity, protectionType);
        }
        return damage;
    }

    private static boolean isBluntDamage(DamageSource source) {
        return source.getDirectEntity() != null;
    }

    private static float applyMagicProtection(float damage, LivingEntity entity, String protectionType) {
        return applyProtectionForType(damage, entity, protectionType, "vminus:magic_protection");
    }

    private static float applyFallProtection(float damage, LivingEntity entity, String protectionType) {
        return applyProtectionForType(damage, entity, protectionType, "vminus:fall_protection");
    }

    private static float applyFireProtection(float damage, LivingEntity entity, String protectionType) {
        return applyProtectionForType(damage, entity, protectionType, "vminus:fire_protection");
    }

    private static float applyBluntProtection(float damage, LivingEntity entity, String protectionType) {
        return applyProtectionForType(damage, entity, protectionType, "vminus:blunt_protection");
    }

    private static float applyProtectionForType(float damage, LivingEntity entity, String protectionType, String protectionTag) {
        float protectionPercentage = ProtectionHelper.getTotalProtectionValue(entity, protectionType);
        return Math.min(damage * 0.99f, Math.max(0, damage - (damage * protectionPercentage)));
    }

    public static float getTotalProtectionValue(Entity entity, String protectionType) {
        if (!(entity instanceof LivingEntity livingEntity) || protectionType == null) {
            return 0;
        }
        float prot = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = livingEntity.getItemBySlot(slot);
            float itemProtection = getProtectionValueFromItem(itemStack, slot, protectionType);
            float generalProtection = getProtectionValueFromItem(itemStack, slot, "vminus:protection");
            prot += itemProtection + generalProtection;
        }
        return Math.max(Math.min(prot, 100), 0);
    }

    private static float getProtectionValueFromItem(ItemStack itemStack, EquipmentSlot slot, String attributeName) {
        if (itemStack.isEmpty()) {
            return 0.0f;
        }
        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attributeName));
        if (attribute == null) {
            return 0.0f;
        }
        Collection<AttributeModifier> modifiers = itemStack.getAttributeModifiers(slot).get(attribute);
        if (!modifiers.isEmpty()) {
            return (float) modifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
        }
        return 0.0f;
    }
}