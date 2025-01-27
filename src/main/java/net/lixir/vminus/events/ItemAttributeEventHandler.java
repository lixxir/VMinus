package net.lixir.vminus.events;

import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.registry.VMinusAttributes;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import java.util.*;

@Mod.EventBusSubscriber
public class ItemAttributeEventHandler {
    private static final ConcurrentHashMap<String, UUID> UUID_CACHE = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void addAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack itemStack = event.getItemStack();
        String itemId = ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString();
        JsonObject visionData = Vision.getData(itemStack);
        EquipmentSlot eventSlot = event.getSlotType();
        boolean miningFlag = false;

        if (visionData != null) {
            int index = 0;
            while (true) {

                JsonObject attributeObject = VisionProperties.findSearchObject(VisionProperties.Names.ATTRIBUTE, visionData, index);
                if (attributeObject == null)
                    break;
                index++;
                String id = VisionProperties.getString(attributeObject, visionData, VisionProperties.Names.ID, itemStack);
                if (id == null)
                    continue;

                boolean replace = VisionProperties.getBoolean(attributeObject, visionData, VisionProperties.Names.REPLACE, itemStack, false);
                boolean remove = VisionProperties.getBoolean(attributeObject, visionData, VisionProperties.Names.REMOVE, itemStack, false);

                if (replace || remove) {
                    Multimap<Attribute, AttributeModifier> originalModifiers = event.getOriginalModifiers();
                    for (Attribute a : originalModifiers.keySet()) {
                        for (AttributeModifier modifier : originalModifiers.get(a)) {
                            String modifierId = ForgeRegistries.ATTRIBUTES.getKey(a).toString();
                            if (modifierId.equals(id)) {
                                event.removeModifier(a, modifier);
                            }
                        }
                    }
                    if (remove)
                        continue;
                }

                String operationId = VisionProperties.getString(attributeObject, visionData, VisionProperties.Names.OPERATION, itemStack);
                AttributeModifier.Operation operation = getOperation(operationId);

                Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(id));

                if (attribute != null) {
                    String compositeId = itemId + "|" + id  + "|" + operation;

                    UUID uuid = UUID_CACHE.computeIfAbsent(compositeId, k -> UUID.randomUUID());
                    EquipmentSlot slot = EquipmentSlot.MAINHAND;
                    String slotId = VisionProperties.getString(attributeObject, visionData, VisionProperties.Names.SLOT, itemStack);
                    if (slotId != null && !slotId.isEmpty()) {
                        slot = EquipmentSlot.valueOf(slotId.toUpperCase());
                    } else if (itemStack.getItem() instanceof Equipable equipable) {
                        slot = equipable.getEquipmentSlot();
                    }
                    if (eventSlot != slot) {
                        continue;
                    }

                    String name = VisionProperties.getString(attributeObject, visionData, VisionProperties.Names.NAME, itemStack, id);
                    if (name == null) {
                        if (id.contains(".")) {
                            name = id.substring(id.indexOf(".") + 1);
                        } else if (id.contains(":")) {
                            name = id.substring(id.indexOf(":") + 1);
                        }
                        if (name != null)
                            name = name.replaceAll("_", " ");
                    }
                    if (name == null)
                        continue;

                    double value = VisionProperties.getNumber(attributeObject, visionData, VisionProperties.Names.VALUE, itemStack).doubleValue();

                    AttributeModifier modifier = new AttributeModifier(uuid, name, value, operation);
                    boolean found = false;
                    for (Attribute a : event.getModifiers().keySet()) {
                        Collection<AttributeModifier> attributeModifiers = event.getModifiers().get(a);
                        for (AttributeModifier m : attributeModifiers) {
                            if (m.equals(modifier)) {
                                found = true;
                                break;
                            }
                        }
                        if (found)
                            break;
                    }
                    if (found)
                        continue;
                    if (id.equals("vminus:mining_speed"))
                        miningFlag = true;
                    event.addModifier(attribute, modifier);
                }
            }
        }

        if (eventSlot == EquipmentSlot.MAINHAND) {

            handleMiningAttributes(event, itemStack, miningFlag);
        }
    }

    private static void handleMiningAttributes(ItemAttributeModifierEvent event, ItemStack itemStack, boolean miningFlag) {
        int efficiencyLevel = EnchantmentHelper.getEnchantments(itemStack).getOrDefault(Enchantments.BLOCK_EFFICIENCY, 0);
        if (efficiencyLevel > 0) {
            double miningSpeedValue = efficiencyLevel * efficiencyLevel + 1;
            AttributeModifier miningSpeedModifier = new AttributeModifier(UUID.fromString("83e34d00-65ae-11ef-814d-325096b39f47"), "Efficiency Mining Speed", miningSpeedValue, AttributeModifier.Operation.ADDITION);
            event.addModifier(VMinusAttributes.MININGSPEED.get(), miningSpeedModifier);
        }
        if (itemStack.getItem() instanceof TieredItem tieredItem) {
            if (!miningFlag) {
                double tierMiningSpeed = tieredItem.getTier().getSpeed();
                AttributeModifier tierMiningSpeedModifier = new AttributeModifier(UUID.fromString("e14d7c20-65ae-11ef-814d-325096b39f47"), "Tier Mining Speed", tierMiningSpeed, AttributeModifier.Operation.ADDITION);
                event.addModifier(VMinusAttributes.MININGSPEED.get(), tierMiningSpeedModifier);
            }
        }
    }

    private static AttributeModifier.@NotNull Operation getOperation(String operationId) {
        if (operationId == null)
            return AttributeModifier.Operation.ADDITION;
        return switch (operationId) {
            case "multiply_base" -> AttributeModifier.Operation.MULTIPLY_BASE;
            case "multiply_total" -> AttributeModifier.Operation.MULTIPLY_TOTAL;
            default -> AttributeModifier.Operation.ADDITION;
        };
    }
}