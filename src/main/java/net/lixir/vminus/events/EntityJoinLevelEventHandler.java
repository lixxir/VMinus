package net.lixir.vminus.events;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionPropertyTypes;
import net.lixir.vminus.vision.util.VisionBaseAttribute;
import net.lixir.vminus.vision.util.VisionItemReplacement;
import net.lixir.vminus.vision.util.VisionUtil;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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
            VisionDuck visionDuck = (VisionDuck) livingEntity;
            Vision vision = Vision.getVision(visionDuck);
            List<VisionBaseAttribute> baseAttributeValues = vision.getValues(VisionPropertyTypes.Entities.BASE_ATTRIBUTE, new VisionContext(livingEntity));
            for (VisionBaseAttribute visionBaseAttribute : baseAttributeValues) {
                Attribute attribute = visionBaseAttribute.attribute();
                Double value = visionBaseAttribute.value();
                AttributeInstance attributeInstance = livingEntity.getAttribute(attribute);
                if (attributeInstance != null)
                    attributeInstance.setBaseValue(value);
            }
        }

        Boolean ban = VisionUtil.getOverrideValue((VisionDuck) entity, VisionPropertyTypes.Entities.BAN, new VisionContext(entity));
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
            VisionItemReplacement visionItemReplacement = VisionUtil.getOverrideValue(visionDuck, VisionPropertyTypes.Items.REPLACE, visionContext);
            Boolean itemBan = VisionUtil.getOverrideValue(visionDuck, VisionPropertyTypes.Items.BAN, visionContext);

            if (visionItemReplacement != null) {
                ItemStack replacementStack = visionItemReplacement.itemStack();
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
