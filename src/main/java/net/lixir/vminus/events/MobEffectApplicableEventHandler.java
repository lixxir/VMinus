package net.lixir.vminus.events;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionValueHandler;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.living.PotionEvent;

@Mod.EventBusSubscriber
public class MobEffectApplicableEventHandler {
    @SubscribeEvent
    public static void onMobEffectEvent(PotionEvent event) {
        if (event != null && event.getEntity() != null) {
            MobEffectInstance effectInstance = event.getPotionEffect();
            if (effectInstance != null) {
                MobEffect effect = effectInstance.getEffect();
                JsonObject visionData = VisionHandler.getVisionData(effect);
                if (visionData != null && visionData.has("banned")) {
                    boolean banned = VisionValueHandler.isBooleanMet(visionData, "banned");
                    if (banned) {
                        if (event.hasResult()) {
                            event.setResult(Event.Result.DENY);
                        }
                    }
                }
            }
        }
    }
}