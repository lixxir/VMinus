package net.lixir.vminus.entity.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class Effect extends MobEffect {
    public Effect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    public boolean effectAdded(@NotNull LivingEntity entity, @Nullable Entity source, @NotNull MobEffectInstance mobEffectInstance, @Nullable MobEffectInstance oldMobEffectInstance) {
        return false;
    }

    public boolean effectRemoved(@NotNull LivingEntity entity, MobEffect mobEffect, MobEffectInstance mobEffectInstance) {
        return false;
    }

    public boolean effectExpired(@NotNull LivingEntity entity, MobEffect mobEffect, MobEffectInstance mobEffectInstance) {
        return false;
    }
}
