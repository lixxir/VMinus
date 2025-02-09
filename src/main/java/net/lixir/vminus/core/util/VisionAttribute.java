package net.lixir.vminus.core.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record VisionAttribute(Boolean remove, Boolean replace, AttributeModifier attributeModifier, Attribute attribute,
                              EquipmentSlot equipmentSlot, String id) {
}
