package net.lixir.vminus.procedures;

import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Collection;

@Mod.EventBusSubscriber
public class HealthLostStatBoostMiningSpeedProcedure {
    @SubscribeEvent
    public static void onBlockBreaking(PlayerEvent.BreakSpeed event) {
        if (event.getPosition().isEmpty())
            return;
        execute(event, event.getNewSpeed(), event.getEntity());
    }

    public static void execute(double breakSpeed, Entity entity) {
        execute(null, breakSpeed, entity);
    }

    private static void execute(@Nullable Event event, double breakSpeed, Entity entity) {
        if (entity == null)
            return;
        if (event instanceof PlayerEvent.BreakSpeed _speed) {
            LivingEntity livingEntity = (LivingEntity) entity;
            float modifierSum = 0.0f;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack itemStack = livingEntity.getItemBySlot(slot);
                if (!itemStack.isEmpty()) {
                    for (int i = (int) livingEntity.getMaxHealth(); i > livingEntity.getHealth(); i--) {
                        modifierSum += getAttributeValueFromItem(itemStack, slot, VMinusAttributes.HEALTHLOSTSTATBOOST.get());
                    }
                }
            }
            System.out.println("is this thing on?");
            System.out.println(modifierSum);
            _speed.setNewSpeed((float) breakSpeed + ((float) breakSpeed * modifierSum));
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
