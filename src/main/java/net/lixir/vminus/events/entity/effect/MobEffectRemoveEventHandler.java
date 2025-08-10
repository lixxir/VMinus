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
public class MobEffectRemoveEventHandler {
    @SubscribeEvent
    public static void onEffectRemove(MobEffectEvent.@NotNull Remove event) {
        if (event.getEffect() instanceof Effect effect) {
            LivingEntity entity = event.getEntity();
            MobEffectInstance mobEffectInstance = event.getEffectInstance();
            MobEffect mobEffect = event.getEffect();
            if (effect.effectRemoved(entity, mobEffect, mobEffectInstance) && event.isCancelable())
                event.setCanceled(true);
        }
    }
}
