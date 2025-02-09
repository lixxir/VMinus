package net.lixir.vminus.events;

import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChangeAttackTargetEventHandler {
    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity entity = event.getOriginalTarget();
        LivingEntity sourceEntity = event.getEntity();
        if (entity == null || sourceEntity == null) return;
        double distance = entity.distanceTo(sourceEntity);
        double trackingRange = getTrackingRange(sourceEntity);
        double hostileAttraction = getHostileAttractionValue(entity);
        float translucency = entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*0.75f;
        double cutoffRange = (trackingRange - (trackingRange * (hostileAttraction * -1 / 100.0))) - (trackingRange * translucency);
        if (distance > cutoffRange) {
            if (event.isCancelable()) {
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
        if (entity.getAttribute(VMinusAttributes.MOB_DETECTION_RANGE.get()) != null) {
            return entity.getAttribute(VMinusAttributes.MOB_DETECTION_RANGE.get()).getBaseValue();
        }
        return 0.0;
    }
}
