package net.lixir.vminus.core.resources.deserializers;

import com.google.gson.*;
import net.lixir.vminus.core.visions.ItemVision;
import net.lixir.vminus.core.VisionType;
import net.lixir.vminus.core.resources.VisionProcessor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.UseAnim;

import java.lang.reflect.Type;

public class ItemVisionDeserializer implements JsonDeserializer<ItemVision> {

    public ItemVision deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        ItemVision vision = new ItemVision();
        vision.mergeEntries(VisionProcessor.getEntries(jsonObject, VisionType.ITEM));

        if (jsonObject.has(vision.maxStackSize.getName()))
            VisionProcessor.parseInt(jsonObject, vision.maxStackSize.getName(), vision.maxStackSize, 1, Integer.MAX_VALUE);
        if (jsonObject.has(vision.maxDamage.getName()))
            VisionProcessor.parseInt(jsonObject, vision.maxDamage.getName(), vision.maxDamage, 1, Integer.MAX_VALUE);
        if (jsonObject.has(vision.enchantability.getName()))
            VisionProcessor.parseInt(jsonObject, vision.enchantability.getName(), vision.enchantability, 0, Integer.MAX_VALUE);
        if (jsonObject.has(vision.useDuration.getName()))
            VisionProcessor.parseInt(jsonObject, vision.useDuration.getName(), vision.useDuration, 0, Integer.MAX_VALUE);
        if (jsonObject.has(vision.fuelTime.getName()))
            VisionProcessor.parseInt(jsonObject, vision.fuelTime.getName(), vision.fuelTime, 0, Integer.MAX_VALUE);

        if (jsonObject.has(vision.fireResistant.getName()))
            VisionProcessor.parseBoolean(jsonObject, vision.fireResistant.getName(), vision.fireResistant);
        if (jsonObject.has(vision.canEquip.getName()))
            VisionProcessor.parseBoolean(jsonObject, vision.canEquip.getName(), vision.canEquip);
        if (jsonObject.has(vision.damageable.getName()))
            VisionProcessor.parseBoolean(jsonObject, vision.damageable.getName(), vision.damageable);
        if (jsonObject.has(vision.enchantable.getName()))
            VisionProcessor.parseBoolean(jsonObject, vision.enchantable.getName(), vision.enchantable);
        if (jsonObject.has(vision.hasGlint.getName()))
            VisionProcessor.parseBoolean(jsonObject, vision.hasGlint.getName(), vision.hasGlint);

        if (jsonObject.has(vision.foodProperties.getName()))
            VisionProcessor.parseVisionFoodProperties(jsonObject, vision.foodProperties.getName(), vision.foodProperties);
        if (jsonObject.has(vision.useAnimation.getName()))
            VisionProcessor.parseEnum(jsonObject, vision.useAnimation.getName(), vision.useAnimation, UseAnim.class);
        if (jsonObject.has(vision.equipSlot.getName()))
            VisionProcessor.parseEnum(jsonObject, vision.equipSlot.getName(), vision.equipSlot, EquipmentSlot.class);
        if (jsonObject.has(vision.rarity.getName()))
            VisionProcessor.parseRarity(jsonObject, vision.rarity.getName(), vision.rarity);
        if (jsonObject.has(vision.attribute.getName()))
            VisionProcessor.parseVisionAttribute(jsonObject, vision.attribute.getName(), vision.attribute);

        return vision;
    }

}