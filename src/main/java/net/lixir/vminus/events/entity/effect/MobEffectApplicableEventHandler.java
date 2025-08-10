package net.lixir.vminus.events.entity.effect;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
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
            Boolean ban = VisionUtils.getOverrideValue((VisionDuck) effect, VisionProperties.Effects.BAN, new VisionContext(effect));
            if (ban != null && ban) {
                if (event.hasResult()) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
