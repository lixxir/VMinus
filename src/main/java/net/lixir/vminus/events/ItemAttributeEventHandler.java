package net.lixir.vminus.events;

import com.google.common.collect.Multimap;
import net.lixir.vminus.core.conditions.VisionConditionArguments;
import net.lixir.vminus.core.util.VisionAttribute;
import net.lixir.vminus.core.visions.ItemVision;
import net.lixir.vminus.core.visions.visionable.IItemVisionable;
import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import java.util.*;

@Mod.EventBusSubscriber
public class ItemAttributeEventHandler {
    private static final ConcurrentHashMap<String, UUID> UUID_CACHE = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void addAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();
        EquipmentSlot eventSlot = event.getSlotType();
        boolean miningFlag = false;
        if (item instanceof IItemVisionable iVisionable) {
            ItemVision itemVision = iVisionable.vminus$getVision();
            List<VisionAttribute> visionAttributes = itemVision.attribute.getValues(new VisionConditionArguments.Builder().passItemStack(itemStack).build());


            for (VisionAttribute visionAttribute : visionAttributes) {
                boolean replace = visionAttribute.replace();
                boolean remove = visionAttribute.remove();

                if (replace || remove) {
                    Multimap<Attribute, AttributeModifier> originalModifiers = event.getOriginalModifiers();
                    for (Attribute a : originalModifiers.keySet()) {
                        for (AttributeModifier modifier : originalModifiers.get(a)) {
                            String modifierId = Objects.requireNonNull(ForgeRegistries.ATTRIBUTES.getKey(a)).toString();
                            if (modifierId.equals(visionAttribute.id())) {
                                event.removeModifier(a, modifier);
                            }
                        }
                    }
                    if (remove)
                        continue;
                }
                EquipmentSlot equipmentSlot = visionAttribute.equipmentSlot();
                if (equipmentSlot == null) {
                    if (item instanceof Equipable equipable) {
                        equipmentSlot = equipable.getEquipmentSlot();
                    } else {
                        equipmentSlot = EquipmentSlot.MAINHAND;
                    }
                }
                if (eventSlot == equipmentSlot)
                    event.addModifier(visionAttribute.attribute(), visionAttribute.attributeModifier());
            }
        }


        /*
            index = 0;
            while (true) {
                String traitId = VisionProperties.getString(visionData, VisionProperties.Names.TRAIT, itemStack, index);
                if (traitId == null)
                    break;
                index++;
                if (!traitId.contains("="))
                    continue;
                String validId = traitId.substring(0, traitId.indexOf('='));
                boolean value;
                if (traitId.endsWith("true")) {
                    value = true;
                } else  if (traitId.endsWith("false")) {
                    value = false;
                } else {
                    continue;
                }

                Trait trait = Traits.TRAIT_REGISTRY.get().getValue(new ResourceLocation(validId));
                if (trait == null)
                    continue;
                if (!Traits.hasTrait(itemStack, trait))
                    Traits.setTrait(itemStack, trait, value);
            }


         */
        if (eventSlot == EquipmentSlot.MAINHAND) {

            handleMiningAttributes(event, itemStack, miningFlag);
        }


    }

    private static void handleMiningAttributes(ItemAttributeModifierEvent event, ItemStack itemStack, boolean miningFlag) {
        int efficiencyLevel = EnchantmentHelper.getEnchantments(itemStack).getOrDefault(Enchantments.BLOCK_EFFICIENCY, 0);
        if (efficiencyLevel > 0) {
            double miningSpeedValue = efficiencyLevel * efficiencyLevel + 1;
            AttributeModifier miningSpeedModifier = new AttributeModifier(UUID.fromString("83e34d00-65ae-11ef-814d-325096b39f47"), "Efficiency Mining Speed", miningSpeedValue, AttributeModifier.Operation.ADDITION);
            event.addModifier(VMinusAttributes.MINING_SPEED.get(), miningSpeedModifier);
        }
        if (itemStack.getItem() instanceof TieredItem tieredItem) {
            if (!miningFlag) {
                double tierMiningSpeed = tieredItem.getTier().getSpeed();
                AttributeModifier tierMiningSpeedModifier = new AttributeModifier(UUID.fromString("e14d7c20-65ae-11ef-814d-325096b39f47"), "Tier Mining Speed", tierMiningSpeed, AttributeModifier.Operation.ADDITION);
                event.addModifier(VMinusAttributes.MINING_SPEED.get(), tierMiningSpeedModifier);
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