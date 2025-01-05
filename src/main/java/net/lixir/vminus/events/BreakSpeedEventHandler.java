package net.lixir.vminus.events;

import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber
public class BreakSpeedEventHandler {
    @SubscribeEvent
    public static void onBlockBreaking(PlayerEvent.BreakSpeed event) {
        if (event.getPosition().isEmpty())
            return;
        double breakSpeed = event.getNewSpeed();
        LivingEntity entity = event.getEntity();
        if (entity == null)
            return;
        float modifierSum = 0.0f;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = entity.getItemBySlot(slot);
            if (!itemStack.isEmpty()) {
                for (int i = (int) entity.getMaxHealth(); i > entity.getHealth(); i--) {
                    modifierSum += getAttributeValueFromItem(itemStack, slot, VMinusAttributes.HEALTHLOSTSTATBOOST.get());
                }
            }
        }
        event.setNewSpeed((float) breakSpeed + ((float) breakSpeed * modifierSum));
    }


    private static float getAttributeValueFromItem(ItemStack itemStack, EquipmentSlot slot, Attribute attribute) {
        Collection<AttributeModifier> modifiers = itemStack.getAttributeModifiers(slot).get(attribute);
        if (!modifiers.isEmpty()) {
            return (float) modifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
        }
        return 0.0f;
    }
}
