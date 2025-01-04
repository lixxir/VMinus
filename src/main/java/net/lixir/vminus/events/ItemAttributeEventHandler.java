package net.lixir.vminus.events;

import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.registry.VMinusAttributes;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionValueHandler;
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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public class ItemAttributeEventHandler {
    private static final ConcurrentHashMap<String, UUID> UUID_CACHE = new ConcurrentHashMap<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack itemstack = event.getItemStack();
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
                        if (attributeData.has("id") && attributeData.has("value")) {
                            String attributeId = attributeData.get("id").getAsString();
                            String attributeName = "";
                            if (attributeData.has("name")) {
                                attributeName = attributeData.get("name").getAsString();
                            } else {
                                if (attributeId.indexOf(".") != -1) {
                                    attributeName = attributeId.substring(attributeId.indexOf(".") + 1);
                                } else if (attributeId.indexOf(":") != -1) {
                                    attributeName = attributeId.substring(attributeId.indexOf(":") + 1);
                                }
                                attributeName = attributeName.replaceAll("_", " ");
                            }

                            String operationString = attributeData.has("operation") ? attributeData.get("operation").getAsString().toLowerCase() : "addition";
                            double attributeValue = attributeData.get("value").getAsDouble();
                            boolean replace = attributeData.has("replace") && attributeData.get("replace").getAsBoolean();
                            if (replace) {
                                if (event != null) {
                                    Multimap<Attribute, AttributeModifier> originalModifiers = event.getOriginalModifiers();
                                    for (Attribute attribute : originalModifiers.keySet()) {
                                        for (AttributeModifier modifier : originalModifiers.get(attribute)) {
                                            String modifierId = ForgeRegistries.ATTRIBUTES.getKey(attribute).toString();
                                            if (modifierId.equals(attributeId)) {
                                                event.removeModifier(attribute, modifier);
                                            }
                                        }
                                    }
                                }
                            }

                            AttributeModifier.Operation operation;
                            switch (operationString) {
                                case "multiply_base":
                                    operation = AttributeModifier.Operation.MULTIPLY_BASE;
                                    break;
                                case "multiply_total":
                                    operation = AttributeModifier.Operation.MULTIPLY_TOTAL;
                                    break;
                                default:
                                    operation = AttributeModifier.Operation.ADDITION;
                                    break;
                            }
                            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attributeId));
                            if (attribute != null) {
                                UUID attributeUUID;
                                String compositeId = itemId + "|" + attributeId;
                                // caching & getting uuids
                                if (attributeData.has("uuid")) {
                                    attributeUUID = UUID.fromString(attributeData.get("uuid").getAsString());
                                } else if (UUID_CACHE.get(compositeId) != null) {
                                    attributeUUID = UUID_CACHE.get(compositeId);
                                } else {
                                    UUID randomUUID = UUID.randomUUID();
                                    UUID_CACHE.put(compositeId, randomUUID);
                                    attributeUUID = randomUUID;
                                }
                                EquipmentSlot slot = EquipmentSlot.MAINHAND;
                                if (attributeData.has("slot")) {
                                    slot = EquipmentSlot.valueOf(attributeData.get("slot").getAsString().toUpperCase());
                                } else {
                                    // attempted fallback to get the equipment slot if not defined.
                                    if (itemstack.is(ItemTags.create(new ResourceLocation("forge", "armors/helmets")))) {
                                        slot = EquipmentSlot.HEAD;
                                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("forge", "armors/chestplates")))) {
                                        slot = EquipmentSlot.CHEST;
                                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("forge", "armors/leggings")))) {
                                        slot = EquipmentSlot.LEGS;
                                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("forge", "armors/boots")))) {
                                        slot = EquipmentSlot.FEET;
                                    }
                                }
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
            ItemStack copyStack = VisionValueHandler.setNBTs(itemData, itemstack.copy());
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
                    event.addModifier(VMinusAttributes.MININGSPEED.get(), miningSpeedModifier);
                }
            }
            if (!miningFlag) {
                if (itemstack.getItem() instanceof TieredItem tieredItem) {
                    double tierMiningSpeed = tieredItem.getTier().getSpeed();
                    AttributeModifier tierMiningSpeedModifier = new AttributeModifier(UUID.fromString("e14d7c20-65ae-11ef-814d-325096b39f47"), "Tier Mining Speed", tierMiningSpeed, AttributeModifier.Operation.ADDITION);
                    if (event != null) {
                        event.addModifier(VMinusAttributes.MININGSPEED.get(), tierMiningSpeedModifier);
                    }
                }
            }
        }
    }
}
