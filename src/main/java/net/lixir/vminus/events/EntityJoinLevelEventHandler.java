package net.lixir.vminus.events;

import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityJoinLevelEventHandler {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        /*
        Entity entity = event.getEntity();
        if (entity == null)
            return;
        Level level = event.getLevel();
        EntityVision vision = EntityVision.of(entity);
        Boolean banned = vision.ban.value(new VisionContext(entity));

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

            VisionContext visionContext = new VisionContext.Builder().pass(stack).pass(entity).build();
            VisionItemReplacement visionItemReplacement = ItemVision.of(item).replace.value(visionContext);
            if (visionItemReplacement == null)
                return;
            ItemStack replacementStack = visionItemReplacement.itemStack();
            Boolean itemBanned = ItemVision.of(stack).ban.value(new VisionContext(stack));

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
            List<VisionBaseAttribute> baseAttributeValues = vision.base_attribute.values(new VisionContext(entity));
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

         */
    }
}
