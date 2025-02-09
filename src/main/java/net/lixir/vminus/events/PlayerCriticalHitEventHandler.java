package net.lixir.vminus.events;

import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber
public class PlayerCriticalHitEventHandler {
    private static final float DEFAULT_DAMAGE_MODIFIER = 1.5f;
    private static final float DETOUR_DAMAGE_MODIFIER = 1.15f;

    @SubscribeEvent
    public static void onPlayerCriticalHit(CriticalHitEvent event) {
        LivingEntity entity = event.getEntity();
        event.setDamageModifier((ModList.get().isLoaded("detour") ? DETOUR_DAMAGE_MODIFIER : DEFAULT_DAMAGE_MODIFIER) + getAttributeModifiers(entity));
    }

    private static float getAttributeModifiers(LivingEntity entity) {
        float modifierSum = 0.0f;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = entity.getItemBySlot(slot);
            if (!itemStack.isEmpty()) {
                modifierSum += getCritValueFromItem(itemStack, slot, VMinusAttributes.CRITICAL_DAMAGE.get());
            }
        }
        if (ModList.get().isLoaded("detour")) {
            float fallDistance = entity.fallDistance;
            float fallDistanceModifier = Math.min(fallDistance * 0.05f, 0.70f);
            modifierSum += fallDistanceModifier;
        }
        return modifierSum;
    }

    private static float getCritValueFromItem(ItemStack itemStack, EquipmentSlot slot, Attribute attribute) {
        Collection<AttributeModifier> modifiers = itemStack.getAttributeModifiers(slot).get(attribute);
        if (modifiers != null && !modifiers.isEmpty()) {
            return (float) modifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
        }
        return 0.0f;
    }
}
