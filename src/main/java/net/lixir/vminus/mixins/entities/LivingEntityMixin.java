package net.lixir.vminus.mixins.entities;

import com.google.gson.JsonObject;
import net.lixir.vminus.traits.Traits;
import net.lixir.vminus.core.Visions;
import net.lixir.vminus.core.VisionProperties;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Inject(method = "canFreeze", at = @At("RETURN"), cancellable = true)
    public void canFreeze(CallbackInfoReturnable<Boolean> cir) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack armorPiece = vminus$entity.getItemBySlot(slot);
                if (Traits.hasTrait(armorPiece, Traits.INSULATED.get())) {
                    cir.setReturnValue(Traits.getTrait(armorPiece, Traits.INSULATED.get()));
                    return;
                }
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
                JsonObject visionData = Visions.getData(effectInstance.getEffect());
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

    @Inject(method = "isSensitiveToWater", at = @At("RETURN"), cancellable = true)
    private void isSensitiveToWater(CallbackInfoReturnable<Boolean> cir) {
        if (VisionProperties.searchElement(VisionProperties.Names.WATER_SENSITIVE, vminus$entity) != null)
            cir.setReturnValue(VisionProperties.getBoolean(VisionProperties.Names.WATER_SENSITIVE, vminus$entity, cir.getReturnValue()));
    }

    @Inject(method = "canBreatheUnderwater", at = @At("RETURN"), cancellable = true)
    private void canBreatheUnderwater(CallbackInfoReturnable<Boolean> cir) {
        if (VisionProperties.searchElement(VisionProperties.Names.UNDERWATER_BREATHING, vminus$entity) != null)
            cir.setReturnValue(VisionProperties.getBoolean(VisionProperties.Names.UNDERWATER_BREATHING, vminus$entity, cir.getReturnValue()));
    }

    @Inject(method = "getSoundVolume", at = @At("RETURN"), cancellable = true)
    private void getSoundVolume(CallbackInfoReturnable<Float> cir) {
        if (VisionProperties.searchElement(VisionProperties.Names.VOLUME, vminus$entity) != null)
            cir.setReturnValue(Math.max(0f, VisionProperties.getNumber(VisionProperties.Names.VOLUME, vminus$entity, cir.getReturnValue()).floatValue()));
    }

    @Inject(method = "getExperienceReward", at = @At("HEAD"), cancellable = true)
    private void getExperienceReward(CallbackInfoReturnable<Integer> cir) {
        if (VisionProperties.searchElement(VisionProperties.Names.XP, vminus$entity) != null)
            cir.setReturnValue(Math.max(0, VisionProperties.getNumber(VisionProperties.Names.XP, vminus$entity, cir.getReturnValue()).intValue()));
    }


}
