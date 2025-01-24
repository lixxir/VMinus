package net.lixir.vminus.events;

import com.google.gson.JsonObject;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.util.VisionValueHandler;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MobEffectApplicableEventHandler {
    @SubscribeEvent
    public static void onMobEffectEvent(MobEffectEvent.Applicable event) {
        if (event != null && event.getEntity() != null) {
            MobEffect effect = event.getEffectInstance().getEffect();
            if (effect == null)
                return;
            JsonObject visionData = Vision.getData(effect);
            if (visionData != null && visionData.has("banned")) {
                boolean banned = VisionValueHandler.isBooleanMet(visionData, "banned");
                if (banned) {
                    if (event != null && event.hasResult()) {
                        event.setResult(Event.Result.DENY);
                    }
                }
            }
        }
    }
}
