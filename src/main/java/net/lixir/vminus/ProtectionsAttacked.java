package net.lixir.vminus;

import net.lixir.vminus.procedures.ProtectionHelperProcedure;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ProtectionsAttacked {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null)
            return;
        DamageSource damageSource = event.getSource();
        double amount = event.getAmount();
        double damage = amount;
        damage = applyProtection(damage, entity, damageSource, "vminus:fire_protection", "vminus:protection/fire");
        damage = applyProtection(damage, entity, damageSource, "vminus:blast_protection", "vminus:protection/blast");
        damage = applyProtection(damage, entity, damageSource, "vminus:magic_protection", "vminus:protection/magic");
        damage = applyProtection(damage, entity, damageSource, "vminus:fall_protection", "vminus:protection/fall");
        damage = applyProtection(damage, entity, damageSource, "vminus:blunt_protection", "vminus:protection/blast");
        if (damage != amount) {
            event.setAmount((float) damage);
        }
    }

    private static double applyProtection(double damage, LivingEntity entity, DamageSource source, String protectionType, String protectionTag) {
        if (source.is(TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(protectionTag))) || protectionType.equals("vminus:protection")) {
            double protectionPercentage = ProtectionHelperProcedure.execute(entity, protectionType);
            double reducedDamage = Math.min(damage * 0.99, Math.max(0, damage - (damage * protectionPercentage)));
            return reducedDamage;
        }
        return damage;
    }
}
