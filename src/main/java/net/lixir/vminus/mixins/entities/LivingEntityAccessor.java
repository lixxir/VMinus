package net.lixir.vminus.mixins.entities;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;
import java.util.Map;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("activeEffects")
    Map<MobEffect, MobEffectInstance> getActiveEffects();

    @Accessor("effectsDirty")
    boolean isEffectsDirty();

    @Accessor("effectsDirty")
    void setEffectsDirty(boolean effectsDirty);

    @Invoker("onEffectUpdated")
    void callOnEffectUpdated(MobEffectInstance instance, boolean apply, @Nullable Entity source);

    @Invoker("onEffectRemoved")
    void callOnEffectRemoved(MobEffectInstance instance);

    @Invoker("updateInvisibilityStatus")
    void callUpdateInvisibilityStatus();

    @Invoker("updateGlowingStatus")
    void callUpdateGlowingStatus();
}
