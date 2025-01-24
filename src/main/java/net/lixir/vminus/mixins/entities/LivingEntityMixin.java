package net.lixir.vminus.mixins.entities;

import com.google.gson.JsonObject;
import net.lixir.vminus.SoundHelper;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.util.VisionValueHandler;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Unique
    private final LivingEntity vminus$entity = (LivingEntity) (Object) this;


    // Automatic custom loot tables for variants
    @Inject(method = "getLootTable", at = @At("HEAD"), cancellable = true)
    public void getLootTable(CallbackInfoReturnable<ResourceLocation> cir) {
        vminus$entity.getPersistentData();
        if (vminus$entity.getPersistentData().contains("variant")) {
            String variant = vminus$entity.getPersistentData().getString("variant");
            String entityName = ForgeRegistries.ENTITY_TYPES.getKey(vminus$entity.getType()).getPath();
            if (!variant.equals("normal")) {
                ResourceLocation customLoot = new ResourceLocation("vminus:entities/variant/" + entityName + "/" + variant);
                cir.setReturnValue(customLoot);
            }
        }
    }

    @Inject(method = "tickEffects", at = @At("HEAD"), cancellable = true)
    private void tickEffects(CallbackInfo ci) {
        LivingEntityAccessor accessor = (LivingEntityAccessor) vminus$entity;
        Iterator<MobEffect> iterator = accessor.getActiveEffects().keySet().iterator();
        try {
            while (iterator.hasNext()) {
                MobEffect mobeffect = iterator.next();
                MobEffectInstance mobeffectinstance = accessor.getActiveEffects().get(mobeffect);
                if (!mobeffectinstance.tick(vminus$entity, () -> {
                    accessor.callOnEffectUpdated(mobeffectinstance, true, null);
                })) {
                    if (!vminus$entity.level().isClientSide) {
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
            if (!vminus$entity.level().isClientSide) {
                accessor.callUpdateInvisibilityStatus();
                accessor.callUpdateGlowingStatus();
            }
            accessor.setEffectsDirty(false);
        }
        int i = -1;
        boolean flag1 = false;

        for (MobEffectInstance effectInstance : accessor.getActiveEffects().values()) {
            if (!effectInstance.getEffect().isInstantenous()) {
                JsonObject visionData = Vision.getData(effectInstance.getEffect());
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
                double d0 = (double) (i >> 16 & 255) / 255.0D;
                double d1 = (double) (i >> 8 & 255) / 255.0D;
                double d2 = (double) (i >> 0 & 255) / 255.0D;
                vminus$entity.level().addParticle(flag1 ? ParticleTypes.AMBIENT_ENTITY_EFFECT : ParticleTypes.ENTITY_EFFECT,
                        vminus$entity.getRandomX(0.5D), vminus$entity.getRandomY(), vminus$entity.getRandomZ(0.5D), d0, d1, d2);
            }
        }
        ci.cancel();
    }

    @Inject(method = "isSensitiveToWater", at = @At("HEAD"), cancellable = true)
    private void isSensitiveToWater(CallbackInfoReturnable<Boolean> cir) {
        JsonObject visionData = Vision.getData(vminus$entity.getType());
        if (visionData != null && visionData.has("water_sensitive")) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(visionData, "water_sensitive", vminus$entity));
        }
    }

    @Inject(method = "canDisableShield", at = @At("HEAD"), cancellable = true)
    private void canDisableShield(CallbackInfoReturnable<Boolean> cir) {
        JsonObject visionData = Vision.getData(vminus$entity.getType());
        if (visionData != null && visionData.has("disable_shields")) {
            System.out.println("DISABLING SHIELDS!");
            cir.setReturnValue(VisionValueHandler.isBooleanMet(visionData, "disable_shields", vminus$entity));
        }
    }

    @Inject(method = "canBreatheUnderwater", at = @At("HEAD"), cancellable = true)
    private void canBreatheUnderwater(CallbackInfoReturnable<Boolean> cir) {
        JsonObject visionData = Vision.getData(vminus$entity.getType());
        if (visionData != null && visionData.has("underwater_breathing")) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(visionData, "underwater_breathing", vminus$entity));
        }
    }

    @Inject(method = "getSoundVolume", at = @At("HEAD"), cancellable = true)
    private void getSoundVolume(CallbackInfoReturnable<Float> cir) {
        JsonObject visionData = Vision.getData(vminus$entity.getType());
        if (visionData != null && visionData.has("volume")) {
            float defaultVolume = 1f;
            float totalVolume = VisionValueHandler.isNumberMet(visionData, "volume", defaultVolume, vminus$entity);
            if (totalVolume != defaultVolume) {
                cir.setReturnValue(Math.max(totalVolume, 0.0f));
            }
        }
    }

    @Inject(method = "getExperienceReward", at = @At("HEAD"), cancellable = true)
    private void getExperienceReward(CallbackInfoReturnable<Integer> cir) {
        JsonObject visionData = Vision.getData(vminus$entity.getType());
        if (visionData != null && visionData.has("experience")) {
            int experience = (int) VisionValueHandler.isNumberMet(visionData, "experience", 0, vminus$entity);
            System.out.println("DROPPING NEW XP: " + experience);
            cir.setReturnValue(Math.max(experience, 0));
        }
    }

    @Nullable
    @Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
    private void getDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        JsonObject visionData = Vision.getData(vminus$entity.getType());
        if (visionData != null && visionData.has("death_sound")) {
            System.out.println("Trying deathsound");
            String soundString = VisionValueHandler.getFirstValidString(visionData, "death_sound", vminus$entity);
            SoundEvent sound = SoundHelper.getSoundEventFromString(soundString);
            if (sound != null)
                cir.setReturnValue(sound);
        }
    }

    @Nullable
    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void getHurtSound(DamageSource p_219440_, CallbackInfoReturnable<SoundEvent> cir) {
        JsonObject visionData = Vision.getData(vminus$entity.getType());
        if (visionData != null && visionData.has("hurt_sound")) {
            String soundString = VisionValueHandler.getFirstValidString(visionData, "hurt_sound", vminus$entity);
            SoundEvent sound = SoundHelper.getSoundEventFromString(soundString);
            if (sound != null)
                cir.setReturnValue(sound);
        }
    }
}
