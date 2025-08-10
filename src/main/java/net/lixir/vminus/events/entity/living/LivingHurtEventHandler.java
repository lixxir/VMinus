package net.lixir.vminus.events.entity.living;

import net.lixir.vminus.registry.util.VMinusTags;
import net.lixir.vminus.util.AttributeHelper;
import net.lixir.vminus.entity.attribute.VMinusAttributes;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Mod.EventBusSubscriber
public class LivingHurtEventHandler {
    private static final List<ProtectionConfig> PROTECTION_CONFIGS = List.of(
            new ProtectionConfig(VMinusAttributes.FIRE_PROTECTION, VMinusTags.DamageTypes.FIRE_DAMAGE),
            new ProtectionConfig(VMinusAttributes.BLAST_PROTECTION, VMinusTags.DamageTypes.BLAST_DAMAGE),
            new ProtectionConfig(VMinusAttributes.MAGIC_PROTECTION, VMinusTags.DamageTypes.MAGIC_DAMAGE),
            new ProtectionConfig(VMinusAttributes.FALL_PROTECTION, VMinusTags.DamageTypes.FALL_DAMAGE),
            new ProtectionConfig(VMinusAttributes.BLUNT_PROTECTION, VMinusTags.DamageTypes.BLUNT_DAMAGE)
    );
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingHurt(@NotNull LivingHurtEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity livingEntity))
            return;

        DamageSource damageSource = event.getSource();
        float damage = event.getAmount();

        for (ProtectionConfig protectionConfig : PROTECTION_CONFIGS) {
            damage = AttributeHelper.applyProtection(damage, livingEntity, damageSource, protectionConfig.attribute(), protectionConfig.damageTag());
        }

        event.setAmount(damage);
    }

    private record ProtectionConfig(Attribute attribute, TagKey<DamageType> damageTag) {}
}
