package net.lixir.vminus.events;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.util.AttributeHelper;
import net.lixir.vminus.registry.VMinusAttributes;
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
        if (!(entity instanceof LivingEntity livingEntity))
            return;

        DamageSource damageSource = event.getSource();
        float damage = event.getAmount();

        List<ProtectionConfig> protectionTypes = List.of(
                new ProtectionConfig(VMinusAttributes.FIRE_PROTECTION.get(), new ResourceLocation(VMinus.ID, "protection/fire")),
                new ProtectionConfig(VMinusAttributes.MAGIC_PROTECTION.get(), new ResourceLocation(VMinus.ID, "protection/magic")),
                new ProtectionConfig(VMinusAttributes.FALL_PROTECTION.get(), new ResourceLocation(VMinus.ID, "protection/fall")),
                new ProtectionConfig(VMinusAttributes.BLUNT_PROTECTION.get(), new ResourceLocation(VMinus.ID, "protection/blunt"))
        );

        for (ProtectionConfig protectionConfig : protectionTypes) {
            damage = AttributeHelper.applyProtection(damage, livingEntity, damageSource, protectionConfig.attribute(), protectionConfig.damageTag());
        }

        event.setAmount(damage);
    }


    private record ProtectionConfig(Attribute attribute, ResourceLocation damageTag) {}
}
