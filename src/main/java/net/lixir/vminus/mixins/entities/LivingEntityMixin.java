package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.item.trait.ItemTraits;
import net.lixir.vminus.attribute.VMinusAttributes;
import net.lixir.vminus.util.VariantEntity;
import net.lixir.vminus.util.SizeAttributeUtil;
import net.lixir.vminus.visions.EffectVision;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements VariantEntity {
    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Unique
    private ResourceLocation vminus$variantTexture = null;

    @Unique
    private ResourceLocation vminus$variantName = null;

    @Override
    public void vminus$setVariant(@Nullable ResourceLocation name, @Nullable ResourceLocation texture) {
        if (name != null && name.getPath().isEmpty()) {
            vminus$variantName = null;
        } else {
            vminus$variantName = name;
        }
        if (texture != null && texture.getPath().isEmpty()) {
            vminus$variantTexture = null;
        } else {
            vminus$variantTexture = texture;
        }
    }

    @Override
    public @Nullable ResourceLocation vminus$getVariantTexture() {
        return vminus$variantTexture;
    }

    @Override
    public @Nullable ResourceLocation vminus$getVariantName() {
        return vminus$variantName;
    }

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

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void vminus$addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putString("VariantTexture",  vminus$variantTexture == null ? "null" : vminus$variantTexture.toString());
        compoundTag.putString("VariantName", vminus$variantName == null ? "null" : vminus$variantName.toString());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void vminus$readAdditionalSaveDataCompoundTag(CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.contains("VariantName"))
            vminus$variantName = compoundTag.getString("VariantName").equals("null") ? null : ResourceLocation.parse(compoundTag.getString("VariantName"));
        if (compoundTag.contains("VariantTexture"))
            vminus$variantTexture = compoundTag.getString("VariantTexture").equals("null") ? null : ResourceLocation.parse(compoundTag.getString("VariantTexture"));
    }

    @Inject(method = "getJumpBoostPower", at = @At("RETURN"), cancellable = true)
    public void getJumpBoostPower(CallbackInfoReturnable<Float> cir) {
        float increase = 0;
        if (vminus$entity.getAttributes().hasAttribute(VMinusAttributes.JUMP_BOOST))
            increase += (float) vminus$entity.getAttributeValue(VMinusAttributes.JUMP_BOOST) * 0.15f;
        if (increase != 0)
            cir.setReturnValue((cir.getReturnValue() * increase) * SizeAttributeUtil.getHeight(vminus$entity) * 0.2f);
    }

    @Inject(method = "getEyeHeight", at = @At("RETURN"), cancellable = true)
    public void getEyeHeight(CallbackInfoReturnable<Float> callbackInfo) {
        if (vminus$entity == null)
            return;
        if (vminus$entity.tickCount > 0) {
            float height = SizeAttributeUtil.getHeight(vminus$entity);
            if (height != 1) {
                callbackInfo.setReturnValue(callbackInfo.getReturnValue() * height);
            }
        }
    }

    @Inject(method = "getDimensions", at = @At(value = "RETURN"), cancellable = true)
    public void getDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        float defaultHeight = cir.getReturnValue().height;
        float defaultWidth = cir.getReturnValue().width;
        float width = defaultWidth * SizeAttributeUtil.getWidth(vminus$entity);
        float height = defaultHeight * SizeAttributeUtil.getHeight(vminus$entity);
        if (width != defaultWidth && height != defaultHeight)
            cir.setReturnValue(EntityDimensions.scalable(width, height));
    }

    @Inject(method = "baseTick", at = @At(value = "TAIL"))
    public void baseTick(CallbackInfo ci) {
        if (vminus$entity == null)
            return;
        vminus$entity.refreshDimensions();
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void createLivingAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue().add(VMinusAttributes.WIDTH);
        cir.getReturnValue().add(VMinusAttributes.HEIGHT);
        cir.getReturnValue().add(VMinusAttributes.PROTECTION);
        cir.getReturnValue().add(VMinusAttributes.FALL_PROTECTION);
        cir.getReturnValue().add(VMinusAttributes.FIRE_PROTECTION);
        cir.getReturnValue().add(VMinusAttributes.BLUNT_PROTECTION);
        cir.getReturnValue().add(VMinusAttributes.BLAST_PROTECTION);
        cir.getReturnValue().add(VMinusAttributes.MAGIC_PROTECTION);
    }


    @Inject(method = "canFreeze", at = @At("RETURN"), cancellable = true)
    public void canFreeze(CallbackInfoReturnable<Boolean> cir) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack armorPiece = vminus$entity.getItemBySlot(slot);
                if (ItemTraits.hasTrait(armorPiece, ItemTraits.INSULATED.get())) {
                    cir.setReturnValue(ItemTraits.getTrait(armorPiece, ItemTraits.INSULATED.get()));
                    return;
                }
            }
        }
    }

    @Redirect(
            method = "tickEffects",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
            )
    )
    private void vminus$tickEffects(Level level, ParticleOptions original, double x, double y, double z, double dx, double dy, double dz) {

        List<MobEffectInstance> effects = vminus$entity.getActiveEffects().stream().toList();
        List<ParticleType<?>> customParticles = new ArrayList<>();

        for (MobEffectInstance instance : effects) {
            MobEffect effect = instance.getEffect();
            EffectVision vision = EffectVision.of(effect);

            if (vision.particle != null) {
                ParticleType<?> particleType = vision.particle.value(new VisionConditionArguments(effect));

                if (particleType != null) {
                    for (int i = 0; i < 1; i++) {
                        customParticles.add(particleType);
                    }
                }
            }
        }
        if (customParticles.isEmpty()) {
            level.addParticle(original, x, y, z, dx, dy, dz);
        } else {
            ParticleOptions chosen = (ParticleOptions) customParticles.get(vminus$entity.getRandom().nextInt(customParticles.size()));
            dx = ((random.nextFloat() * 2) - 1f) * 0.3;
            dy = (random.nextFloat() * 0.2) + 0.1;
            dz = ((random.nextFloat() * 2) - 1f) * 0.3;
            level.addParticle(chosen, x, y, z, dx, dy, dz);
        }
    }

    @Inject(method = "canBreatheUnderwater", at = @At("RETURN"), cancellable = true)
    private void canBreatheUnderwater(CallbackInfoReturnable<Boolean> cir) {
          /*
        if (VisionProperties.searchElement(VisionProperties.Names.UNDERWATER_BREATHING, vminus$entity) != null)
            cir.setReturnValue(VisionProperties.getBoolean(VisionProperties.Names.UNDERWATER_BREATHING, vminus$entity, cir.getReturnValue()));

           */
    }

    @Inject(method = "getSoundVolume", at = @At("RETURN"), cancellable = true)
    private void getSoundVolume(CallbackInfoReturnable<Float> cir) {
          /*
        if (VisionProperties.searchElement(VisionProperties.Names.VOLUME, vminus$entity) != null)
            cir.setReturnValue(Math.max(0f, VisionProperties.getNumber(VisionProperties.Names.VOLUME, vminus$entity, cir.getReturnValue()).floatValue()));

           */
    }

    @Inject(method = "getExperienceReward", at = @At("HEAD"), cancellable = true)
    private void getExperienceReward(CallbackInfoReturnable<Integer> cir) {
          /*
        if (VisionProperties.searchElement(VisionProperties.Names.XP, vminus$entity) != null)
            cir.setReturnValue(Math.max(0, VisionProperties.getNumber(VisionProperties.Names.XP, vminus$entity, cir.getReturnValue()).intValue()));

           */
    }


}
