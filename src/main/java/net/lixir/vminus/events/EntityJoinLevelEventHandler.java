package net.lixir.vminus.events;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.core.conditions.VisionConditionArguments;
import net.lixir.vminus.core.util.VisionBaseAttribute;
import net.lixir.vminus.core.visions.EntityVision;
import net.lixir.vminus.core.visions.accessors.IEntityVisionAccessor;
import net.lixir.vminus.core.visions.accessors.IItemVisionAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class EntityJoinLevelEventHandler {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity == null)
            return;
        if (entity instanceof IEntityVisionAccessor iVisionable) {
            EntityVision vision = iVisionable.vminus$getVision();
            Boolean banned = iVisionable.vminus$getVision().ban.value(new VisionConditionArguments(entity));

            // Banning banned entities
            if (banned != null && banned) {
                if (event.isCancelable()) {
                    event.setCanceled(true);
                } else if (event.hasResult()) {
                    event.setResult(Event.Result.DENY);
                }
                return;
            }


            if (entity instanceof LivingEntity livingEntity) {
                List<VisionBaseAttribute> baseAttributeValues = vision.baseAttribute.values(new VisionConditionArguments(entity));
                for (VisionBaseAttribute visionBaseAttribute : baseAttributeValues) {
                    Attribute attribute = visionBaseAttribute.attribute();
                    Double value = visionBaseAttribute.value();
                    VMinus.LOGGER.info(attribute);
                    VMinus.LOGGER.info(value);
                    AttributeInstance attributeInstance = livingEntity.getAttribute(attribute);
                    if (attributeInstance != null) {
                        attributeInstance.setBaseValue(value);
                    }
                }
                // Adjust health from setting new health
                if (!entity.getPersistentData().contains("health_adjust") || !entity.getPersistentData().getBoolean("health_adjust")) {
                    float value = (float) livingEntity.getAttributeBaseValue(Attributes.MAX_HEALTH);
                    if (value > 0) {
                        livingEntity.setHealth(value);
                        entity.getPersistentData().putBoolean("health_adjust", true);
                    }
                }
            }
        }
        if (entity instanceof ItemEntity itemEntity) {
            ItemStack itemstack = itemEntity.getItem();
            Item item = itemstack.getItem();
            if (!(item instanceof IItemVisionAccessor iItemVisionAccessor))
                return;

            VisionConditionArguments visionConditionArguments = new VisionConditionArguments.Builder().passItemStack(itemstack).passEntity(entity).build();
            ItemStack replacementStack = iItemVisionAccessor.vminus$getVision().replace.value(visionConditionArguments);
            Boolean itemBanned = iItemVisionAccessor.vminus$getVision().ban.value(visionConditionArguments);

            if (replacementStack != null && !replacementStack.isEmpty()) {
                replacementStack.setCount(itemstack.getCount());

                if (event.isCancelable()) {
                    event.setCanceled(true);
                } else if (event.hasResult()) {
                    event.setResult(Event.Result.DENY);
                }
                Level world = entity.level();
                ItemEntity newItemEntity = new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), replacementStack);
                newItemEntity.setDeltaMovement(entity.getDeltaMovement());
                world.addFreshEntity(newItemEntity);

            } else if (itemBanned != null && itemBanned) {
                if (event.isCancelable()) {
                    event.setCanceled(true);
                } else if (event.hasResult()) {
                    event.setResult(Event.Result.DENY);
                }
            }

        }


        // Adjusting max health for when attributes are set for entities.

/*
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;

        if (visionData != null && visionData.has("variants")) {
            final String chosenVariant = MobVariantHelper.setOrGetVariant(entity, visionData);

            serverLevel.getServer().execute(() -> {
                VMinus.PACKET_HANDLER.send(
                        PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                        new MobVariantSyncPacket(entity.getId(), chosenVariant)
                );
            });
            VMinus.queueServerWork(1, () -> {
                serverLevel.getServer().execute(() -> {
                    VMinus.PACKET_HANDLER.send(
                            PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                            new MobVariantSyncPacket(entity.getId(), chosenVariant)
                    );
                });
            });
        }

 */

    }
}
