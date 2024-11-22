package net.lixir.vminus.mixins.entities;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionValueHelper;
import net.lixir.vminus.SoundHelper;
import net.lixir.vminus.visions.VisionHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    private final Map<MobEffect, MobEffectInstance> activeEffects = Maps.newHashMap();
    @Unique
    private final LivingEntity entity = (LivingEntity) (Object) this;

    @Shadow
    protected abstract void spawnItemParticles(ItemStack itemstack, int count);

    @Inject(method = "breakItem", at = @At("HEAD"), cancellable = true)
    private void breakItem(ItemStack itemstack, CallbackInfo ci) {
        String soundString = VisionValueHelper.getFirstValidString(null, "break_sound", itemstack);
        if (soundString != null && !soundString.isEmpty()) {
            if (!itemstack.isEmpty()) {
                if (!((LivingEntity) (Object) this).isSilent()) {
                    ResourceLocation customSound = new ResourceLocation(soundString);
                    SoundEvent customSoundEvent = BuiltInRegistries.SOUND_EVENT.getOptional(customSound).orElse(SoundEvents.ITEM_BREAK);
                    ((LivingEntity) (Object) this).level().playLocalSound(((LivingEntity) (Object) this).getX(), ((LivingEntity) (Object) this).getY(), ((LivingEntity) (Object) this).getZ(), customSoundEvent,
                            ((LivingEntity) (Object) this).getSoundSource(), 0.8F, 0.8F + ((LivingEntity) (Object) this).level().random.nextFloat() * 0.4F, false);
                }
                if (!VisionValueHelper.isBooleanMet(null, "no_break_particles", itemstack))
                    this.spawnItemParticles(itemstack, 5);
            }
            ci.cancel();
        }
    }

    @Inject(method = "tickEffects", at = @At("HEAD"), cancellable = true)
    private void tickEffects(CallbackInfo ci) {
        LivingEntityAccessor accessor = (LivingEntityAccessor) entity;
        Iterator<MobEffect> iterator = accessor.getActiveEffects().keySet().iterator();
        try {
            while (iterator.hasNext()) {
                MobEffect mobeffect = iterator.next();
                MobEffectInstance mobeffectinstance = accessor.getActiveEffects().get(mobeffect);
                if (!mobeffectinstance.tick(entity, () -> {
                    accessor.callOnEffectUpdated(mobeffectinstance, true, null);
                })) {
                    if (!entity.level().isClientSide) {
                        iterator.remove();
                        accessor.callOnEffectRemoved(mobeffectinstance);
                    }
                } else if (mobeffectinstance.getDuration() % 600 == 0) {
                    accessor.callOnEffectUpdated(mobeffectinstance, false, null);
                }
            }
        } catch (ConcurrentModificationException ignored) {
        }
        if (accessor.isEffectsDirty()) {
            if (!entity.level().isClientSide) {
                accessor.callUpdateInvisibilityStatus();
                accessor.callUpdateGlowingStatus();
            }
            accessor.setEffectsDirty(false);
        }
        int i = -1;
        boolean flag1 = false;

        for (MobEffectInstance effectInstance : accessor.getActiveEffects().values()) {
            if (!effectInstance.getEffect().isInstantenous()) {
                JsonObject visionData = VisionHandler.getVisionData(effectInstance.getEffect());
                if (visionData == null || !visionData.has("particle")) {
                    i = effectInstance.getEffect().getColor();
                    flag1 = effectInstance.getEffect().getCategory() == MobEffectCategory.BENEFICIAL;
                    break;
                }
            }
        }

        if (i > 0) {
            Random random = new Random(System.nanoTime());
            boolean flag = random.nextBoolean();

            if (flag1) {
                flag &= random.nextInt(8) == 0;
            }

            if (flag && i > 0) {
                double d0 = (double)(i >> 16 & 255) / 255.0D;
                double d1 = (double)(i >> 8 & 255) / 255.0D;
                double d2 = (double)(i >> 0 & 255) / 255.0D;
                entity.level().addParticle(flag1 ? ParticleTypes.AMBIENT_ENTITY_EFFECT : ParticleTypes.ENTITY_EFFECT,
                        entity.getRandomX(0.5D), entity.getRandomY(), entity.getRandomZ(0.5D), d0, d1, d2);
            }
        }
        ci.cancel();
    }

    @Inject(method = "isSensitiveToWater", at = @At("HEAD"), cancellable = true)
    private void isSensitiveToWater(CallbackInfoReturnable<Boolean> cir) {
        JsonObject visionData = VisionHandler.getVisionData(entity);
        if (visionData != null && visionData.has("water_sensitive")) {
            cir.setReturnValue(VisionValueHelper.isBooleanMet(visionData, "water_sensitive", entity));
        }
    }

    @Inject(method = "canDisableShield", at = @At("HEAD"), cancellable = true)
    private void canDisableShield(CallbackInfoReturnable<Boolean> cir) {
        JsonObject visionData = VisionHandler.getVisionData(entity);
        if (visionData != null && visionData.has("disable_shields")) {
            System.out.println("DISABLING SHIELDS!");
            cir.setReturnValue(VisionValueHelper.isBooleanMet(visionData, "disable_shields", entity));
        }
    }

    @Inject(method = "canBreatheUnderwater", at = @At("HEAD"), cancellable = true)
    private void canBreatheUnderwater(CallbackInfoReturnable<Boolean> cir) {
        JsonObject visionData = VisionHandler.getVisionData(entity);
        if (visionData != null && visionData.has("underwater_breathing")) {
            cir.setReturnValue(VisionValueHelper.isBooleanMet(visionData, "underwater_breathing", entity));
        }
    }

    @Inject(method = "getSoundVolume", at = @At("HEAD"), cancellable = true)
    private void getSoundVolume(CallbackInfoReturnable<Float> cir) {
        JsonObject visionData = VisionHandler.getVisionData(entity);
        if (visionData != null && visionData.has("volume")) {
            float defaultVolume = 1f;
            float totalVolume = VisionValueHelper.isNumberMet(visionData, "volume", defaultVolume, entity);
            if (totalVolume != defaultVolume) {
                cir.setReturnValue(Math.max(totalVolume, 0.0f));
            }
        }
    }

    @Inject(method = "getExperienceReward", at = @At("HEAD"), cancellable = true)
    private void getExperienceReward(CallbackInfoReturnable<Integer> cir) {
        JsonObject visionData = VisionHandler.getVisionData(entity);
        if (visionData != null && visionData.has("experience")) {
            int experience = VisionValueHelper.isNumberMet(visionData, "experience", 0, entity);
            System.out.println("DROPPING NEW XP: " + experience);
            cir.setReturnValue(Math.max(experience, 0));
        }
    }

    @Nullable
    @Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
    private void getDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        JsonObject visionData = VisionHandler.getVisionData(entity);
        if (visionData != null && visionData.has("death_sound")) {
            System.out.println("Trying deathsound");
            String soundString = VisionValueHelper.getFirstValidString(visionData, "death_sound", entity);
            SoundEvent sound = SoundHelper.getSoundEventFromString(soundString);
            if (sound != null)
                cir.setReturnValue(sound);
        }
    }

    @Nullable
    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void getHurtSound(DamageSource p_219440_, CallbackInfoReturnable<SoundEvent> cir) {
        JsonObject visionData = VisionHandler.getVisionData(entity);
        if (visionData != null && visionData.has("hurt_sound")) {
            String soundString = VisionValueHelper.getFirstValidString(visionData, "hurt_sound", entity);
            SoundEvent sound = SoundHelper.getSoundEventFromString(soundString);
            if (sound != null)
                cir.setReturnValue(sound);
        }
    }
}
