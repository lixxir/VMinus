package net.lixir.vminus.mixins.entities;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.lixir.vminus.SoundHelper;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionValueHandler;
import net.minecraft.core.particles.ParticleTypes;
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    private final Map<MobEffect, MobEffectInstance> activeEffects = Maps.newHashMap();
    @Unique
    private final LivingEntity vminus$entity = (LivingEntity) (Object) this;

    @Shadow
    protected abstract void spawnItemParticles(ItemStack itemstack, int count);

    @Inject(method = "breakItem", at = @At("HEAD"), cancellable = true)
    private void breakItem(ItemStack itemstack, CallbackInfo ci) {
        String soundString = VisionValueHandler.getFirstValidString(null, "break_sound", itemstack);
        if (soundString != null && !soundString.isEmpty()) {
            if (!itemstack.isEmpty()) {
                if (!vminus$entity.isSilent()) {
                    ResourceLocation customSound = new ResourceLocation(soundString);
                    SoundEvent customSoundEvent = ForgeRegistries.SOUND_EVENTS.getValue(customSound);
                    if (customSoundEvent == null) {
                        customSoundEvent = SoundEvents.ITEM_BREAK;
                    }

                    vminus$entity.level.playLocalSound(vminus$entity.getX(), vminus$entity.getY(), vminus$entity.getZ(),
                            customSoundEvent, vminus$entity.getSoundSource(), 0.8F, 0.8F + vminus$entity.level.random.nextFloat() * 0.4F, false);
                }
                if (!VisionValueHandler.isBooleanMet(null, "no_break_particles", itemstack))
                    this.spawnItemParticles(itemstack, 5);
            }
            ci.cancel();
        }
    }

    // Automatic custom loot tables for variants
    @Inject(method = "getLootTable", at = @At("HEAD"), cancellable = true)
    public void getLootTable(CallbackInfoReturnable<ResourceLocation> cir) {
        vminus$entity.getPersistentData();
        if (vminus$entity.getPersistentData().contains("variant")) {
            String variant = vminus$entity.getPersistentData().getString("variant");
            String entityName = Objects.requireNonNull(ForgeRegistries.ENTITIES.getKey(vminus$entity.getType())).getPath();
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
                    if (!vminus$entity.level.isClientSide) {
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
            if (!vminus$entity.level.isClientSide) {
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

            if (flag) {
                double d0 = (double) (i >> 16 & 255) / 255.0D;
                double d1 = (double) (i >> 8 & 255) / 255.0D;
                double d2 = (double) (i & 255) / 255.0D;
                vminus$entity.level.addParticle(flag1 ? ParticleTypes.AMBIENT_ENTITY_EFFECT : ParticleTypes.ENTITY_EFFECT,
                        vminus$entity.getRandomX(0.5D), vminus$entity.getRandomY(), vminus$entity.getRandomZ(0.5D), d0, d1, d2);
            }
        }
        ci.cancel();
    }

    @Inject(method = "isSensitiveToWater", at = @At("RETURN"), cancellable = true)
    private void isSensitiveToWater(CallbackInfoReturnable<Boolean> cir) {
        JsonObject visionData = VisionHandler.getVisionData(vminus$entity.getType());
        if (visionData != null && visionData.has("water_sensitive")) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(visionData, "water_sensitive", vminus$entity));
        }
    }

    @Inject(method = "canBreatheUnderwater", at = @At("RETURN"), cancellable = true)
    private void canBreatheUnderwater(CallbackInfoReturnable<Boolean> cir) {
        JsonObject visionData = VisionHandler.getVisionData(vminus$entity.getType());
        if (visionData != null && visionData.has("underwater_breathing")) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(visionData, "underwater_breathing", vminus$entity));
        }
    }

    @Inject(method = "getSoundVolume", at = @At("RETURN"), cancellable = true)
    private void getSoundVolume(CallbackInfoReturnable<Float> cir) {
        JsonObject visionData = VisionHandler.getVisionData(vminus$entity.getType());
        if (visionData != null && visionData.has("volume")) {
            float defaultVolume = 1f;
            float totalVolume = VisionValueHandler.isNumberMet(visionData, "volume", defaultVolume, vminus$entity);
            if (totalVolume != defaultVolume) {
                cir.setReturnValue(Math.max(totalVolume, 0.0f));
            }
        }
    }
}
