package net.lixir.vminus.vision.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record VisionAttribute(Boolean remove, Boolean replace, AttributeModifier attributeModifier, Attribute attribute,
                              @Nullable EquipmentSlot equipmentSlot) {

    public static @NotNull VisionAttribute remove(Attribute attribute) {
        return new VisionAttribute(true, false, null, attribute, null);
    }

    public static @NotNull VisionAttribute replace(Attribute attribute) {
        return new VisionAttribute(false, true, null, attribute, null);
    }

    public static @NotNull VisionAttribute full(AttributeModifier modifier, Attribute attribute, EquipmentSlot slot) {
        return new VisionAttribute(false, false, modifier, attribute, slot);
    }

    public static @NotNull VisionAttribute full(AttributeModifier modifier, Attribute attribute) {
        return full(modifier, attribute, null);
    }

    public static @NotNull VisionAttribute removeAndReplace(AttributeModifier modifier, Attribute attribute, EquipmentSlot slot) {
        return new VisionAttribute(true, true, modifier, attribute, slot);
    }

    public static @NotNull VisionAttribute removeAndReplace(AttributeModifier modifier, Attribute attribute) {
        return removeAndReplace(modifier, attribute, null);
    }

    public static @NotNull VisionAttribute withModifier(Attribute attribute, EquipmentSlot slot, String name, double amount, AttributeModifier.Operation operation) {
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), name, amount, operation);
        return new VisionAttribute(false, false, modifier, attribute, slot);
    }

    public static @NotNull VisionAttribute withModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
        return withModifier(attribute, null, name, amount, operation);
    }

    public static @NotNull VisionAttribute withModifier(Attribute attribute, EquipmentSlot slot, String name, double amount) {
        return withModifier(attribute, slot, name, amount, AttributeModifier.Operation.ADDITION);
    }

    public static @NotNull VisionAttribute withModifier(Attribute attribute, String name, double amount) {
        return withModifier(attribute, null, name, amount, AttributeModifier.Operation.ADDITION);
    }

    public static @NotNull VisionAttribute removeAndReplaceWithModifier(Attribute attribute, EquipmentSlot slot, String name, double amount, AttributeModifier.Operation operation) {
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), name, amount, operation);
        return new VisionAttribute(true, true, modifier, attribute, slot);
    }

    public static @NotNull VisionAttribute removeAndReplaceWithModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
        return removeAndReplaceWithModifier(attribute, null, name, amount, operation);
    }

    public static @NotNull VisionAttribute removeAndReplaceWithModifier(Attribute attribute, EquipmentSlot slot, String name, double amount) {
        return removeAndReplaceWithModifier(attribute, slot, name, amount, AttributeModifier.Operation.ADDITION);
    }

    public static @NotNull VisionAttribute removeAndReplaceWithModifier(Attribute attribute, String name, double amount) {
        return removeAndReplaceWithModifier(attribute, null, name, amount, AttributeModifier.Operation.ADDITION);
    }

    public static @NotNull VisionAttribute fromAttribute(Attribute attribute, EquipmentSlot slot, double amount, AttributeModifier.Operation operation) {
        return withModifier(attribute, slot, attribute.getDescriptionId(), amount, operation);
    }

    public static @NotNull VisionAttribute fromAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation) {
        return fromAttribute(attribute, null, amount, operation);
    }

    public static @NotNull VisionAttribute fromAttribute(Attribute attribute, EquipmentSlot slot, double amount) {
        return fromAttribute(attribute, slot, amount, AttributeModifier.Operation.ADDITION);
    }

    public static @NotNull VisionAttribute fromAttribute(Attribute attribute, double amount) {
        return fromAttribute(attribute, null, amount, AttributeModifier.Operation.ADDITION);
    }
}
