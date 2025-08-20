package net.lixir.vminus.events.entity.living.mob;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class MobSpawnPlacementsEventHandler {
    @SubscribeEvent
    public static void onMobSpawnEvent(MobSpawnEvent.@NotNull SpawnPlacementCheck event) {
        EntityType<?> entityType = event.getEntityType();
        Boolean ban = Vision.getValue(entityType, VisionProperties.Entities.BAN);
        if (Boolean.TRUE.equals(ban)) {
            if (event.isCancelable())
                event.setCanceled(true);
            else if (event.hasResult())
                event.setResult(Event.Result.DENY);
        }
    }
}
