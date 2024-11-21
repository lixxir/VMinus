package net.lixir.vminus.procedures;

import net.minecraft.resources.ResourceLocation;
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
public class ProtectionHelperProcedure {
    public static double execute(Entity entity, String protectionType) {
        if (!(entity instanceof LivingEntity livingEntity) || protectionType == null) {
            return 0;
        }
        double prot = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = livingEntity.getItemBySlot(slot);
            double itemProtection = getProtectionValueFromItem(itemStack, slot, protectionType);
            double generalProtection = getProtectionValueFromItem(itemStack, slot, "vminus:protection");
            prot += itemProtection + generalProtection;
        }
        double finalProtection = Math.max(Math.min(prot, 100), 0);
        return finalProtection;
    }

    private static double getProtectionValueFromItem(ItemStack itemStack, EquipmentSlot slot, String attributeName) {
        if (itemStack.isEmpty()) {
            return 0.0;
        }
        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attributeName));
        if (attribute == null) {
            return 0.0;
        }
        Collection<AttributeModifier> modifiers = itemStack.getAttributeModifiers(slot).get(attribute);
        if (modifiers != null && !modifiers.isEmpty()) {
            double protectionAmount = modifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
            return protectionAmount;
        }
        return 0.0;
    }
}
