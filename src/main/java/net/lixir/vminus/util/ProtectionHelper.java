package net.lixir.vminus.util;

import net.minecraft.core.registries.Registries;
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
    public static float applyProtection(float damage, LivingEntity entity, DamageSource source, String protectionType, String protectionTag) {
        if (source.is(TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(protectionTag))) || protectionType.equals("vminus:protection")) {
            float protectionPercentage = ProtectionHelper.getTotalProtectionValue(entity, protectionType);
            float reducedDamage = Math.min(damage * 0.99f, Math.max(0, damage - (damage * protectionPercentage)));
            return reducedDamage;
        }
        return damage;
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
        float finalProtection = Math.max(Math.min(prot, 100), 0);
        return finalProtection;
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
        if (modifiers != null && !modifiers.isEmpty()) {
            float protectionAmount = (float) modifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
            return protectionAmount;
        }
        return 0.0f;
    }
}
