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

        if (jsonObject.has(vision.maxStackSize.name()))
            VisionProcessor.parseInt(jsonObject, vision.maxStackSize.name(), vision.maxStackSize, 1, Integer.MAX_VALUE);
        if (jsonObject.has(vision.maxDamage.name()))
            VisionProcessor.parseInt(jsonObject, vision.maxDamage.name(), vision.maxDamage, 1, Integer.MAX_VALUE);
        if (jsonObject.has(vision.enchantability.name()))
            VisionProcessor.parseInt(jsonObject, vision.enchantability.name(), vision.enchantability, 0, Integer.MAX_VALUE);
        if (jsonObject.has(vision.useDuration.name()))
            VisionProcessor.parseInt(jsonObject, vision.useDuration.name(), vision.useDuration, 0, Integer.MAX_VALUE);
        if (jsonObject.has(vision.fuelTime.name()))
            VisionProcessor.parseInt(jsonObject, vision.fuelTime.name(), vision.fuelTime, 0, Integer.MAX_VALUE);

        if (jsonObject.has(vision.fireResistant.name()))
            VisionProcessor.parseBoolean(jsonObject, vision.fireResistant.name(), vision.fireResistant);
        if (jsonObject.has(vision.canEquip.name()))
            VisionProcessor.parseBoolean(jsonObject, vision.canEquip.name(), vision.canEquip);
        if (jsonObject.has(vision.damageable.name()))
            VisionProcessor.parseBoolean(jsonObject, vision.damageable.name(), vision.damageable);
        if (jsonObject.has(vision.enchantable.name()))
            VisionProcessor.parseBoolean(jsonObject, vision.enchantable.name(), vision.enchantable);
        if (jsonObject.has(vision.hasGlint.name()))
            VisionProcessor.parseBoolean(jsonObject, vision.hasGlint.name(), vision.hasGlint);
        if (jsonObject.has(vision.ban.name()))
            VisionProcessor.parseBoolean(jsonObject, vision.ban.name(), vision.ban);

        if (jsonObject.has(vision.foodProperties.name()))
            VisionProcessor.parseVisionFoodProperties(jsonObject, vision.foodProperties.name(), vision.foodProperties);
        if (jsonObject.has(vision.useAnimation.name()))
            VisionProcessor.parseEnum(jsonObject, vision.useAnimation.name(), vision.useAnimation, UseAnim.class);
        if (jsonObject.has(vision.equipSlot.name()))
            VisionProcessor.parseEnum(jsonObject, vision.equipSlot.name(), vision.equipSlot, EquipmentSlot.class);
        if (jsonObject.has(vision.rarity.name()))
            VisionProcessor.parseRarity(jsonObject, vision.rarity.name(), vision.rarity);
        if (jsonObject.has(vision.attribute.name()))
            VisionProcessor.parseVisionAttribute(jsonObject, vision.attribute.name(), vision.attribute);
        if (jsonObject.has(vision.replace.name()))
            VisionProcessor.parseItemStack(jsonObject, vision.replace.name(), vision.replace);
        if (jsonObject.has(vision.trait.name()))
            VisionProcessor.parseTrait(jsonObject, vision.trait.name(), vision.trait);
        if (jsonObject.has(vision.decorator.name()))
            VisionProcessor.parseItemDecorator(jsonObject, vision.decorator.name(), vision.decorator);


        return vision;
    }

}