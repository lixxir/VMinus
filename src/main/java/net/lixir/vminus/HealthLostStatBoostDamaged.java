package net.lixir.vminus;

import net.lixir.vminus.init.VminusModAttributes;
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
public class HealthLostStatBoostDamaged {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity entity = event.getSource().getEntity();
        if (entity == null)
            return;
        if (!(entity instanceof LivingEntity _entity))
            return;

        float modifierSum = 0.0f;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = _entity.getItemBySlot(slot);
            if (!itemStack.isEmpty()) {
                for (int i = (int) _entity.getMaxHealth(); i > _entity.getHealth(); i--) {
                    modifierSum += getAttributeValueFromItem(itemStack, slot, VminusModAttributes.HEALTHLOSTSTATBOOST.get());
                }
            }
        }
        double amount = event.getAmount();
        double increasedDamage = amount + (amount * modifierSum);
        if (increasedDamage != amount) {
            event.setAmount((float) increasedDamage);
        }
    }

    private static float getAttributeValueFromItem(ItemStack itemStack, EquipmentSlot slot, Attribute attribute) {
        Collection<AttributeModifier> modifiers = itemStack.getAttributeModifiers(slot).get(attribute);
        if (modifiers != null && !modifiers.isEmpty()) {
            return (float) modifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
        }
        return 0.0f;
    }
}
