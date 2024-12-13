package net.lixir.vminus;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class BannedMobEffects {
    @SubscribeEvent
    public static void onMobEffectEvent(MobEffectEvent.Applicable event) {
        if (event != null && event.getEntity() != null) {
            MobEffect effect = event.getEffectInstance().getEffect();
            if (effect == null)
                return;
            JsonObject visionData = VisionHandler.getVisionData(effect);
            if (visionData != null && visionData.has("banned")) {
                boolean banned = VisionValueHelper.isBooleanMet(visionData, "banned");
                if (banned) {
                    if (event != null && event.hasResult()) {
                        event.setResult(Event.Result.DENY);
                    }
                }
            }
        }
    }
}
