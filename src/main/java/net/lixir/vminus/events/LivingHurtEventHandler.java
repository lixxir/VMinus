package net.lixir.vminus.events;

import net.lixir.vminus.util.ProtectionHelper;
import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber
public class LivingHurtEventHandler {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity entity = event.getSource().getEntity();
        if (entity == null)
            return;
        if (!(entity instanceof LivingEntity _entity))
            return;

        DamageSource damageSource = event.getSource();
        float amount = event.getAmount();
        float damage = amount;

        float modifierSum = 0.0f;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = _entity.getItemBySlot(slot);
            if (!itemStack.isEmpty()) {
                for (int i = (int) _entity.getMaxHealth(); i > _entity.getHealth(); i--) {
                    modifierSum += getAttributeValueFromItem(itemStack, slot, VMinusAttributes.HEALTHLOSTSTATBOOST.get());
                }
            }
        }

        damage = ProtectionHelper.applyProtection(damage, _entity, damageSource, "vminus:fire_protection", "vminus:protection/fire");
        damage = ProtectionHelper.applyProtection(damage, _entity, damageSource, "vminus:blast_protection", "vminus:protection/blast");
        damage = ProtectionHelper.applyProtection(damage, _entity, damageSource, "vminus:magic_protection", "vminus:protection/magic");
        damage = ProtectionHelper.applyProtection(damage, _entity, damageSource, "vminus:fall_protection", "vminus:protection/fall");
        damage = ProtectionHelper.applyProtection(damage, _entity, damageSource, "vminus:blunt_protection", "vminus:protection/blast");

        event.setAmount(damage + (amount * modifierSum));
    }

    private static float getAttributeValueFromItem(ItemStack itemStack, EquipmentSlot slot, Attribute attribute) {
        Collection<AttributeModifier> modifiers = itemStack.getAttributeModifiers(slot).get(attribute);
        if (modifiers != null && !modifiers.isEmpty()) {
            return (float) modifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
        }
        return 0.0f;
    }
}
