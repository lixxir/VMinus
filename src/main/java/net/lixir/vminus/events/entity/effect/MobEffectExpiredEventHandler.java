package net.lixir.vminus.events.entity.effect;

import net.lixir.vminus.entity.effect.Effect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class MobEffectExpiredEventHandler {
    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.@NotNull Expired event) {
        MobEffectInstance mobEffectInstance = event.getEffectInstance();
        if (mobEffectInstance == null)
            return;
        if (event.getEffectInstance().getEffect() instanceof Effect effect) {
            LivingEntity entity = event.getEntity();
            MobEffect mobEffect = mobEffectInstance.getEffect();
            if (effect.effectRemoved(entity, mobEffect, mobEffectInstance) && event.isCancelable())
                event.setCanceled(true);
            if (effect.effectExpired(entity, mobEffect, mobEffectInstance) && event.isCancelable())
                event.setCanceled(true);
        }
    }
}
