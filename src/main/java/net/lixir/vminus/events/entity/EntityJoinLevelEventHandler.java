package net.lixir.vminus.events.entity;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.BaseAttribute;
import net.lixir.vminus.vision.util.ItemReplacement;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Mod.EventBusSubscriber
public class EntityJoinLevelEventHandler {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onEntityJoin(@NotNull EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity == null)
            return;

        if (entity instanceof LivingEntity livingEntity) {
            List<BaseAttribute> baseAttributeValues = Vision.get(livingEntity).getValues(VisionProperties.Entities.BASE_ATTRIBUTE, new VisionContext(livingEntity));
            for (BaseAttribute baseAttribute : baseAttributeValues) {
                Attribute attribute = baseAttribute.attribute();
                Double value = baseAttribute.value();
                AttributeInstance attributeInstance = livingEntity.getAttribute(attribute);
                if (attributeInstance != null)
                    attributeInstance.setBaseValue(value);
            }
        }

        Boolean ban = VisionUtils.getOverrideValue((VisionDuck) entity, VisionProperties.Entities.BAN, new VisionContext(entity));
        // Banning banned entities
        if (ban != null && ban) {
            if (event.isCancelable()) {
                event.setCanceled(true);
            } else if (event.hasResult()) {
                event.setResult(Event.Result.DENY);
            }
            return;
        }

        if (entity instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getItem();
            Item item = stack.getItem();
            VisionDuck visionDuck = (VisionDuck) item;

            VisionContext visionContext = new VisionContext.Builder().pass(stack).pass(entity).build();
            ItemReplacement itemReplacement = VisionUtils.getOverrideValue(visionDuck, VisionProperties.Items.REPLACE, visionContext);
            Boolean itemBan = VisionUtils.getOverrideValue(visionDuck, VisionProperties.Items.BAN, visionContext);

            if (itemReplacement != null) {
                ItemStack replacementStack = itemReplacement.itemStack();
                if (replacementStack != null && !replacementStack.isEmpty()) {
                    replacementStack.setCount(stack.getCount());

                    if (event.isCancelable()) {
                        event.setCanceled(true);
                    } else if (event.hasResult()) {
                        event.setResult(Event.Result.DENY);
                    }
                    Level world = entity.level();
                    ItemEntity newItemEntity = new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), replacementStack);
                    newItemEntity.setDeltaMovement(entity.getDeltaMovement());
                    world.addFreshEntity(newItemEntity);
                }
            } else if (itemBan != null && itemBan) {
                if (event.isCancelable()) {
                    event.setCanceled(true);
                } else if (event.hasResult()) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
