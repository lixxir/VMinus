package net.lixir.vminus.events.entity.effect;

import net.lixir.vminus.world.entity.effect.Effect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class MobEffectAddedEventHandler {
    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.@NotNull Added event) {
        if (event.getEffectInstance().getEffect() instanceof Effect effect) {
            LivingEntity entity = event.getEntity();
            Entity source = event.getEffectSource();
            MobEffectInstance mobEffectInstance = event.getEffectInstance();
            MobEffectInstance oldMobEffectInstance = event.getOldEffectInstance();
            if (effect.effectAdded(entity, source, mobEffectInstance, oldMobEffectInstance) && event.isCancelable())
                event.setCanceled(true);
        }
    }
}
