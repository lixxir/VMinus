package net.lixir.vminus.events.item;

import com.google.common.collect.Multimap;
import net.lixir.vminus.entity.attribute.VMinusAttributes;
import net.lixir.vminus.sight.resource.SightManager;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionAttribute;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mod.EventBusSubscriber
public class ItemAttributeEventHandler {

    @SubscribeEvent
    public static void addAttributeModifier(@NotNull ItemAttributeModifierEvent event) {
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();
        EquipmentSlot eventSlot = event.getSlotType();
        boolean miningFlag = false;

        Vision vision = Vision.get((VisionDuck) item);
        List<VisionAttribute> visionAttributes = vision.getValues(VisionProperties.Items.ATTRIBUTE, new VisionContext(itemStack));
        //List<VisionTrait> visionTraits = vision.trait.values(new VisionContext(itemStack));
        for (VisionAttribute visionAttribute : visionAttributes) {
            boolean replace = visionAttribute.replace();
            boolean remove = visionAttribute.remove();
            if (replace || remove) {
                Multimap<Attribute, AttributeModifier> originalModifiers = event.getOriginalModifiers();
                for (Attribute a : originalModifiers.keySet()) {
                    if (a.equals(VMinusAttributes.MINING_SPEED))
                        miningFlag = true;
                    for (AttributeModifier modifier : originalModifiers.get(a)) {
                        String modifierId = Objects.requireNonNull(ForgeRegistries.ATTRIBUTES.getKey(a)).toString();
                        if (modifierId.equals(visionAttribute.id())) {
                            event.removeModifier(a, modifier);
                        }
                    }
                }
            }
        }

        for (VisionAttribute visionAttribute : visionAttributes) {
            boolean remove = visionAttribute.remove();
            if (remove)
                continue;
            EquipmentSlot equipmentSlot = visionAttribute.equipmentSlot();
            if (equipmentSlot == null) {
                if (item instanceof Equipable equipable) {
                    equipmentSlot = equipable.getEquipmentSlot();
                } else {
                    equipmentSlot = EquipmentSlot.MAINHAND;
                }
            }
            if (eventSlot == equipmentSlot ) {
                if (visionAttribute.attribute().equals(VMinusAttributes.MINING_SPEED))
                        miningFlag = true;
                event.removeModifier(visionAttribute.attribute(), visionAttribute.attributeModifier());
                event.addModifier(visionAttribute.attribute(), visionAttribute.attributeModifier());
            }
        }

        /*
        for (VisionTrait visionTrait : visionTraits) {
            ItemTrait itemTrait = visionTrait.itemTrait();
            boolean value = visionTrait.value();
            if (!ItemTraits.hasTrait(itemStack, itemTrait))
                ItemTraits.setTrait(itemStack, itemTrait, value);
        }

         */


        if (eventSlot == EquipmentSlot.MAINHAND && SightManager.get("mining_attributes")) {
            handleMiningAttributes(event, itemStack, miningFlag);
        }
    }

    private static void handleMiningAttributes(ItemAttributeModifierEvent event, ItemStack itemStack, boolean miningFlag) {
        int efficiencyLevel = EnchantmentHelper.getEnchantments(itemStack).getOrDefault(Enchantments.BLOCK_EFFICIENCY, 0);
        if (efficiencyLevel > 0) {
            double miningSpeedValue = efficiencyLevel * efficiencyLevel + 1;
            AttributeModifier miningSpeedModifier = new AttributeModifier(UUID.fromString("83e34d00-65ae-11ef-814d-325096b39f47"), "Efficiency Mining Speed", miningSpeedValue, AttributeModifier.Operation.ADDITION);
            event.addModifier(VMinusAttributes.MINING_SPEED, miningSpeedModifier);
        }
        if (itemStack.getItem() instanceof TieredItem tieredItem) {
            if (!miningFlag) {
                double tierMiningSpeed = tieredItem.getTier().getSpeed();
                AttributeModifier tierMiningSpeedModifier = new AttributeModifier(UUID.fromString("e14d7c20-65ae-11ef-814d-325096b39f47"), "Tier Mining Speed", tierMiningSpeed, AttributeModifier.Operation.ADDITION);
                event.addModifier(VMinusAttributes.MINING_SPEED, tierMiningSpeedModifier);
            }
        }
    }
}