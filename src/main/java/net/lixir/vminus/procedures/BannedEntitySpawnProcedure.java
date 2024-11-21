package net.lixir.vminus.procedures;

import com.google.gson.JsonObject;
import net.lixir.vminus.core.VisionValueHelper;
import net.lixir.vminus.core.VisionHandler;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class BannedEntitySpawnProcedure {
    @SubscribeEvent
    public static void onEntitySpawned(EntityJoinLevelEvent event) {
        execute(event, event.getEntity());
    }

    public static void execute(Entity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (entity == null)
            return;
        JsonObject visionData = VisionHandler.getVisionData(entity);
        if (VisionValueHelper.isBooleanMet(visionData, "banned", entity)) {
            if (event != null && event.isCancelable()) {
                event.setCanceled(true);
            } else if (event != null && event.hasResult()) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
