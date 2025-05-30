package net.lixir.vminus.events;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.network.VMinusNetworking;
import net.lixir.vminus.network.VariantSyncPacket;
import net.lixir.vminus.util.VariantEntity;
import net.lixir.vminus.visions.EntityVision;
import net.lixir.vminus.visions.ItemVision;
import net.lixir.vminus.visions.accessors.ItemVisionAccessor;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.util.VisionBaseAttribute;
import net.lixir.vminus.visions.util.VisionEntityVariant;
import net.lixir.vminus.visions.util.VisionItemReplacement;
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
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

@Mod.EventBusSubscriber
public class EntityJoinLevelEventHandler {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity == null)
            return;
        Level level = event.getLevel();
        EntityVision vision = EntityVision.of(entity);
        Boolean banned = vision.ban.value(new VisionConditionArguments(entity));

        // Banning banned entities
        if (banned != null && banned) {
            if (event.isCancelable()) {
                event.setCanceled(true);
            } else if (event.hasResult()) {
                event.setResult(Event.Result.DENY);
            }
            return;
        }


        if (entity instanceof ItemEntity itemEntity && itemEntity.getItem().getItem() instanceof ItemVisionAccessor) {
            ItemStack stack = itemEntity.getItem();
            Item item = stack.getItem();

            VisionConditionArguments visionConditionArguments = new VisionConditionArguments.Builder().pass(stack).pass(entity).build();
            VisionItemReplacement visionItemReplacement = ItemVision.of(item).replace.value(visionConditionArguments);
            if (visionItemReplacement == null)
                return;
            ItemStack replacementStack = visionItemReplacement.itemStack();
            Boolean itemBanned = ItemVision.of(stack).ban.value(new VisionConditionArguments(stack));

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

            } else if (itemBanned != null && itemBanned) {
                if (event.isCancelable()) {
                    event.setCanceled(true);
                } else if (event.hasResult()) {
                    event.setResult(Event.Result.DENY);
                }
            }

        }


        if (entity instanceof LivingEntity livingEntity) {
            // Adjust health from setting new health
            if (!entity.getPersistentData().contains("health_adjust") || !entity.getPersistentData().getBoolean("health_adjust")) {
                float value = (float) livingEntity.getAttributeBaseValue(Attributes.MAX_HEALTH);
                if (value > 0) {
                    livingEntity.setHealth(value);
                    entity.getPersistentData().putBoolean("health_adjust", true);
                }
            }
            List<VisionBaseAttribute> baseAttributeValues = vision.base_attribute.values(new VisionConditionArguments(entity));
            for (VisionBaseAttribute visionBaseAttribute : baseAttributeValues) {
                Attribute attribute = visionBaseAttribute.attribute();
                Double value = visionBaseAttribute.value();
                AttributeInstance attributeInstance = livingEntity.getAttribute(attribute);
                if (attributeInstance != null)
                    attributeInstance.setBaseValue(value);
            }
            if (livingEntity instanceof VariantEntity variantEntity) {
                VisionEntityVariant  visionEntityVariant = null;
                if (variantEntity.vminus$getVariantName() == null && variantEntity.vminus$getVariantTexture() == null) {
                    if (!level.isClientSide())
                        visionEntityVariant = VariantEntity.setFromWeightedList(livingEntity);
                }
                if (visionEntityVariant != null && visionEntityVariant.texture() != null && visionEntityVariant.name() != null) {
                    VMinus.queueServerWork(1, () -> VMinusNetworking.CHANNEL.send(
                            PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                            new VariantSyncPacket(entity.getId(), variantEntity.vminus$getVariantName(), variantEntity.vminus$getVariantTexture())
                    ));
                }
            }

        }
    }
}
