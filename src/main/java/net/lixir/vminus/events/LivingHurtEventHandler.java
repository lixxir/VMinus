package net.lixir.vminus.events;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.traits.Traits;
import net.lixir.vminus.util.AttributeHelper;
import net.lixir.vminus.registry.VMinusAttributes;
import net.lixir.vminus.util.ISpeedGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class LivingHurtEventHandler {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity _entity)) {
            return;
        }
        CompoundTag nbt = entity.getPersistentData();
        nbt.putDouble(VMinusAttributes.MOMENTUM_NBT_KEY, 0);

        DamageSource damageSource = event.getSource();
        final float amount = event.getAmount();
        float damage = amount;

        List<ProtectionConfig> protectionTypes = List.of(
                new ProtectionConfig(VMinusAttributes.FIRE_PROTECTION.get(), new ResourceLocation(VMinus.ID, "protection/fire")),
                new ProtectionConfig(VMinusAttributes.MAGIC_PROTECTION.get(), new ResourceLocation(VMinus.ID, "protection/magic")),
                new ProtectionConfig(VMinusAttributes.FALL_PROTECTION.get(), new ResourceLocation(VMinus.ID, "protection/fall")),
                new ProtectionConfig(VMinusAttributes.BLUNT_PROTECTION.get(), new ResourceLocation(VMinus.ID, "protection/blunt"))
        );

        for (ProtectionConfig protectionConfig : protectionTypes) {
            damage = AttributeHelper.applyProtection(damage, _entity, damageSource, protectionConfig.attribute(), protectionConfig.damageTag());
        }

        float healthBoost = 0.0f;



        if (damageSource.getEntity() != null) {
            Entity sourceEntity = damageSource.getEntity();
            CompoundTag sourceNbt = entity.getPersistentData();
            if (sourceNbt.contains(VMinusAttributes.MOMENTUM_NBT_KEY))
                sourceNbt.putDouble(VMinusAttributes.MOMENTUM_NBT_KEY, sourceNbt.getDouble(VMinusAttributes.MOMENTUM_NBT_KEY)*0.5);
            if (sourceEntity instanceof ISpeedGetter speedGetter && sourceEntity instanceof LivingEntity attacker) {
                        if (Traits.hasTrait(attacker.getMainHandItem(), Traits.SURGE.get())) {
                            double speed = Math.max(0, Math.sqrt(speedGetter.vminus$getSpeed()));
                            float speedMultiplier = 1.0f + (float) ((speed / 0.15) * 0.75);
                            damage *= Math.min(speedMultiplier, 5f);
                        }
                }
            }


        event.setAmount(damage + (amount * healthBoost));
    }


    private record ProtectionConfig(Attribute attribute, ResourceLocation damageTag) {}
}
