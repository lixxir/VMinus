package net.lixir.vminus.events.item;

import com.google.common.collect.Multimap;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionAttribute;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Mod.EventBusSubscriber
public class ItemAttributeEventHandler {
    @SubscribeEvent
    public static void addAttributeModifier(final @NotNull ItemAttributeModifierEvent event) {
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();
        EquipmentSlot eventSlot = event.getSlotType();

        Vision vision = Vision.get((VisionDuck) item);
        List<VisionAttribute> visionAttributes = vision.getValues(VisionProperties.Items.ATTRIBUTE, new VisionContext(itemStack));
        for (VisionAttribute visionAttribute : visionAttributes) {
            boolean replace = visionAttribute.replace();
            boolean remove = visionAttribute.remove();
            if (replace || remove) {
                Multimap<Attribute, AttributeModifier> originalModifiers = event.getOriginalModifiers();
                for (Attribute a : originalModifiers.keySet()) {
                    for (AttributeModifier modifier : originalModifiers.get(a)) {
                        if (a == visionAttribute.attribute()) {
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
                event.removeModifier(visionAttribute.attribute(), visionAttribute.attributeModifier());
                event.addModifier(visionAttribute.attribute(), visionAttribute.attributeModifier());
            }
        }
    }
}