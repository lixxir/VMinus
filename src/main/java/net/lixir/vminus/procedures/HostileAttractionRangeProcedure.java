package net.lixir.vminus.procedures;

import net.lixir.vminus.init.VminusModAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class HostileAttractionRangeProcedure {
    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        if (event != null && event.getEntity() != null && event.getOriginalTarget() != null) {
            execute(event, event.getOriginalTarget(), event.getEntity());
        }
    }

    public static void execute(Entity entity, Entity sourceentity) {
        execute(null, entity, sourceentity);
    }

    private static void execute(@Nullable Event event, Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        if (!(entity instanceof LivingEntity targetEntity) || !(sourceentity instanceof LivingEntity attackerEntity))
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
        if (entity.getAttribute(VminusModAttributes.MOBDETECTIONRANGE.get()) != null) {
            return entity.getAttribute(VminusModAttributes.MOBDETECTIONRANGE.get()).getBaseValue();
        }
        return 0.0;
    }
}
