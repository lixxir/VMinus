package net.lixir.vminus.events;

import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChangeAttackTargetEventHandler {
    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        Entity entity = event.getOriginalTarget();
        Entity sourceEntity = event.getEntity();
        if (entity == null || sourceEntity == null) return;
        if (!(entity instanceof LivingEntity targetEntity) || !(sourceEntity instanceof LivingEntity attackerEntity))
            return;
        double distance = targetEntity.distanceTo(attackerEntity);
        double trackingRange = getTrackingRange(attackerEntity);
        double hostileAttraction = getHostileAttractionValue(targetEntity);
        double cutoffRange = trackingRange - (trackingRange * (hostileAttraction * -1 / 100.0));
        if (distance > cutoffRange) {
            if (event != null && event.isCancelable()) {
                event.setCanceled(true);
            }
        }
    }

    private static double getTrackingRange(LivingEntity entity) {
        if (entity.getAttribute(Attributes.FOLLOW_RANGE) != null) {
            return entity.getAttribute(Attributes.FOLLOW_RANGE).getValue();
        }
        return 0.0;
    }

    private static double getHostileAttractionValue(LivingEntity entity) {
        if (entity.getAttribute(VMinusAttributes.MOBDETECTIONRANGE.get()) != null) {
            return entity.getAttribute(VMinusAttributes.MOBDETECTIONRANGE.get()).getBaseValue();
        }
        return 0.0;
    }
}
