package net.lixir.vminus;

import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.init.VminusModAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber
public class AttributeSetting {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addAttributeModifier(ItemAttributeModifierEvent event) {
        execute(event, event.getItemStack());
    }

    public static void execute(ItemStack itemstack) {
        execute(null, itemstack);
    }

    private static void execute(@Nullable ItemAttributeModifierEvent event, ItemStack itemstack) {
        String itemId = ForgeRegistries.ITEMS.getKey(itemstack.getItem()).toString();
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        CompoundTag tag = itemstack.getTag();
        EquipmentSlot eventSlot = (event != null) ? event.getSlotType() : null;
        Boolean miningFlag = false;
        if (itemData != null && (itemData.has("attributes") || itemData.has("tag") || itemData.has("remove_attributes"))) {
            if (itemData.has("remove_attributes")) {
                JsonArray removeAttributesArray = itemData.getAsJsonArray("remove_attributes");
                Set<String> attributesToRemove = new HashSet<>();
                for (JsonElement element : removeAttributesArray) {
                    JsonObject removeData = element.getAsJsonObject();
                    if (removeData.has("id")) {
                        attributesToRemove.add(removeData.get("id").getAsString());
                    }
                }
                if (event != null) {
                    Multimap<Attribute, AttributeModifier> originalModifiers = event.getOriginalModifiers();
                    for (Attribute attribute : originalModifiers.keySet()) {
                        for (AttributeModifier modifier : originalModifiers.get(attribute)) {
                            String modifierId = ForgeRegistries.ATTRIBUTES.getKey(attribute).toString();
                            if (attributesToRemove.contains(modifierId)) {
                                event.removeModifier(attribute, modifier);
                            }
                        }
                    }
                }
            }
            if (itemData.has("attributes")) {
                if (tag == null || !tag.contains("broken")) {
                    JsonArray attributesArray = itemData.getAsJsonArray("attributes");
                    for (JsonElement element : attributesArray) {
                        JsonObject attributeData = element.getAsJsonObject();
                        if (attributeData.has("id") && attributeData.has("value") && attributeData.has("operation")) {
                            String attributeId = attributeData.get("id").getAsString();
                            String attributeName = attributeData.get("name").getAsString();
                            double attributeValue = attributeData.get("value").getAsDouble();
                            String operationString = attributeData.get("operation").getAsString();
                            AttributeModifier.Operation operation = AttributeModifier.Operation.ADDITION;
                            switch (operationString.toLowerCase()) {
                                case "multiply_base":
                                    operation = AttributeModifier.Operation.MULTIPLY_BASE;
                                    break;
                                case "multiply_total":
                                    operation = AttributeModifier.Operation.MULTIPLY_TOTAL;
                                    break;
                                default:
                                case "addition":
                                    operation = AttributeModifier.Operation.ADDITION;
                                    break;
                            }
                            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attributeId));
                            if (attribute != null) {
                                UUID attributeUUID = attributeData.has("uuid") ? UUID.fromString(attributeData.get("uuid").getAsString()) : UUID.randomUUID();
                                EquipmentSlot slot = attributeData.has("slot") ? EquipmentSlot.valueOf(attributeData.get("slot").getAsString().toUpperCase()) : EquipmentSlot.MAINHAND;
                                if (eventSlot == slot) {
                                    AttributeModifier modifier = new AttributeModifier(attributeUUID, attributeName, attributeValue, operation);
                                    if (event != null) {
                                        if (attributeId.equals("vminus:mining_speed"))
                                            miningFlag = true;
                                        event.addModifier(attribute, modifier);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (itemData != null && itemData.has("nbt")) {
            ItemStack copyStack = JsonValueUtil.setNbts(itemData, itemstack.copy());
            //System.out.println(itemData);
            CompoundTag copyTag = copyStack.getOrCreateTag();
            if (copyTag != null && !copyTag.isEmpty()) {
                if (tag == null)
                    tag = itemstack.getOrCreateTag();
                for (String key : copyTag.getAllKeys()) {
                    tag.put(key, copyTag.get(key));
                }
                itemstack.setTag(tag);
            }
        }
        if (ModList.get().isLoaded("detour")) {
            if (itemstack.isEnchantable()) {
                if (tag == null)
                    tag = itemstack.getOrCreateTag();
                if (!tag.contains("enchantment_limit")) {
                    int limit = 2;
                    if (itemstack.is(ItemTags.create(new ResourceLocation("detour:one_enchantment_limit")))) {
                        limit = 1;
                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("detour:three_enchantment_limit")))) {
                        limit = 3;
                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("detour:four_enchantment_limit")))) {
                        limit = 4;
                    }
                    tag.putDouble("enchantment_limit", limit);
                }
            }
        }
        if (eventSlot == EquipmentSlot.MAINHAND) {
            int efficiencyLevel = EnchantmentHelper.getEnchantments(itemstack).getOrDefault(Enchantments.BLOCK_EFFICIENCY, 0);
            if (efficiencyLevel > 0) {
                double miningSpeedValue = efficiencyLevel * efficiencyLevel + 1;
                AttributeModifier miningSpeedModifier = new AttributeModifier(UUID.fromString("83e34d00-65ae-11ef-814d-325096b39f47"), "Effeciency Mining Speed", miningSpeedValue, AttributeModifier.Operation.ADDITION);
                if (event != null) {
                    event.addModifier(VminusModAttributes.MININGSPEED.get(), miningSpeedModifier);
                }
            }
            if (!miningFlag) {
                if (itemstack.getItem() instanceof TieredItem tieredItem) {
                    double tierMiningSpeed = tieredItem.getTier().getSpeed();
                    AttributeModifier tierMiningSpeedModifier = new AttributeModifier(UUID.fromString("e14d7c20-65ae-11ef-814d-325096b39f47"), "Tier Mining Speed", tierMiningSpeed, AttributeModifier.Operation.ADDITION);
                    if (event != null) {
                        event.addModifier(VminusModAttributes.MININGSPEED.get(), tierMiningSpeedModifier);
                    }
                }
            }
        }
    }
}
