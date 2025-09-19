package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.world.entity.attribute.VMinusAttributes;
import net.lixir.vminus.world.item.IEquipmentItem;
import net.lixir.vminus.network.ServerboundJumpPacket;
import net.lixir.vminus.network.VMinusNetwork;
import net.lixir.vminus.resources.data.sight.SightManager;
import net.lixir.vminus.world.entity.VariantEntity;
import net.lixir.vminus.util.SizeAttributeUtil;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements VariantEntity, VisionDuck {
    @Shadow protected boolean jumping;

    @Shadow private int noJumpDelay;

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot p_21127_);

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private ResourceLocation vMinus$variantTexture = null;

    @Unique
    private ResourceLocation vMinus$variantName = null;

    @Override
    public void vMinus$setVariant(@Nullable ResourceLocation name, @Nullable ResourceLocation texture) {
        if (name != null && name.getPath().isEmpty()) {
            vMinus$variantName = null;
        } else {
            vMinus$variantName = name;
        }
        if (texture != null && texture.getPath().isEmpty()) {
            vMinus$variantTexture = null;
        } else {
            vMinus$variantTexture = texture;
        }
    }

    @Unique
    private boolean vminus$wasJumping = false;

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void vminus$onJumpTick(CallbackInfo ci) {


        boolean jumpingNow = this.jumping;
        boolean justPressedJump = jumpingNow && !vminus$wasJumping;

        if (justPressedJump && !this.onGround() && !this.isCrouching() && noJumpDelay == 0) {
            ItemStack boots = this.getItemBySlot(EquipmentSlot.FEET);
            if (boots.getItem() instanceof IEquipmentItem IEquipmentItem) {
                if (level().isClientSide) {
                    IEquipmentItem.onEntityJump(level(), vMinus$self, boots, false);
                    VMinusNetwork.CHANNEL.sendToServer(new ServerboundJumpPacket(this.getId(), false));
                }
            }
        }

        vminus$wasJumping = jumpingNow;
    }


    @Override
    public @Nullable ResourceLocation vMinus$getVariantTexture() {
        return vMinus$variantTexture;
    }

    @Override
    public @Nullable ResourceLocation vMinus$getVariantName() {
        return vMinus$variantName;
    }

    @Unique
    private final LivingEntity vMinus$self = (LivingEntity) (Object) this;

    // Automatic custom loot tables for variants
    @Inject(method = "getLootTable", at = @At("RETURN"), cancellable = true)
    public final void vMinus$getLootTable(CallbackInfoReturnable<ResourceLocation> cir) {
        vMinus$self.getPersistentData();
        if (vMinus$self.getPersistentData().contains("variant")) {
            String variant = vMinus$self.getPersistentData().getString("variant");
            String entityName = ForgeRegistries.ENTITY_TYPES.getKey(vMinus$self.getType()).getPath();
            if (!variant.equals("normal")) {
                ResourceLocation customLoot = new ResourceLocation("vminus:entities/variant/" + entityName + "/" + variant);
                cir.setReturnValue(customLoot);
            }
        }
    }

    @Override
    public @NonNull VisionType<?> vMinus$getVisionType() {
        return VisionTypes.ENTITY;
    }

    @Override
    public @Nullable ResourceLocation vMinus$getVisionId() {
        return ((VisionDuck) getType()).vMinus$getVisionId();
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public final void vMinus$addAdditionalSaveData(@NotNull CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putString("VariantTexture",  vMinus$variantTexture == null ? "null" : vMinus$variantTexture.toString());
        compoundTag.putString("VariantName", vMinus$variantName == null ? "null" : vMinus$variantName.toString());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public final void vMinus$readAdditionalSaveDataCompoundTag(@NotNull CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.contains("VariantName"))
            vMinus$variantName = compoundTag.getString("VariantName").equals("null") ? null : ResourceLocation.parse(compoundTag.getString("VariantName"));
        if (compoundTag.contains("VariantTexture"))
            vMinus$variantTexture = compoundTag.getString("VariantTexture").equals("null") ? null : ResourceLocation.parse(compoundTag.getString("VariantTexture"));
    }

    @Inject(method = "getJumpBoostPower", at = @At("RETURN"), cancellable = true)
    public void getJumpBoostPower(CallbackInfoReturnable<Float> cir) {
        float increase = 0;
        if (vMinus$self.getAttributes().hasAttribute(VMinusAttributes.JUMP_BOOST))
            increase += (float) vMinus$self.getAttributeValue(VMinusAttributes.JUMP_BOOST) * 0.15f;
        if (increase != 0)
            cir.setReturnValue((cir.getReturnValue() * increase) * SizeAttributeUtil.getHeight(vMinus$self) * 0.2f);
    }

    @Unique
    private float vMinus$lastWidthScale = 1.0f;
    @Unique
    private float vMinus$lastHeightScale = 1.0f;

    @Inject(method = "getDimensions", at = @At(value = "RETURN"), cancellable = true)
    public final void vMinus$getDimensions(Pose pose, @NotNull CallbackInfoReturnable<EntityDimensions> cir) {
        if (!SightManager.get("size_attributes"))
            return;
        float defaultHeight = cir.getReturnValue().height;
        float defaultWidth = cir.getReturnValue().width;

        float widthScale = SizeAttributeUtil.getWidth(vMinus$self);
        float heightScale = SizeAttributeUtil.getHeight(vMinus$self);

        float width = defaultWidth * widthScale;
        float height = defaultHeight * heightScale;

        if (width != defaultWidth || height != defaultHeight) {
            cir.setReturnValue(EntityDimensions.scalable(width, height));
        }

        vMinus$lastWidthScale = widthScale;
        vMinus$lastHeightScale = heightScale;
    }

    @Inject(method = "baseTick", at = @At(value = "TAIL"))
    public final void vMinus$baseTick(CallbackInfo ci) {
        if (!SightManager.get("size_attributes"))
            return;
        if (vMinus$self == null)
            return;

        float widthScale = SizeAttributeUtil.getWidth(vMinus$self);
        float heightScale = SizeAttributeUtil.getHeight(vMinus$self);

        if (widthScale != vMinus$lastWidthScale || heightScale != vMinus$lastHeightScale) {
            vMinus$self.refreshDimensions();

            vMinus$lastWidthScale = widthScale;
            vMinus$lastHeightScale = heightScale;
        }
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void createLivingAttributes(@NotNull CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue().add(VMinusAttributes.WIDTH);
        cir.getReturnValue().add(VMinusAttributes.HEIGHT);
        cir.getReturnValue().add(VMinusAttributes.PROTECTION);
        cir.getReturnValue().add(VMinusAttributes.FALL_PROTECTION);
        cir.getReturnValue().add(VMinusAttributes.FIRE_PROTECTION);
        cir.getReturnValue().add(VMinusAttributes.BLUNT_PROTECTION);
        cir.getReturnValue().add(VMinusAttributes.BLAST_PROTECTION);
        cir.getReturnValue().add(VMinusAttributes.MAGIC_PROTECTION);
    }





    /*

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
                ParticleType<?> particleType = vision.particle.value(new VisionContext(effect));

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


    }

    @Inject(method = "getSoundVolume", at = @At("RETURN"), cancellable = true)
    private void getSoundVolume(CallbackInfoReturnable<Float> cir) {
          /*
        if (VisionProperties.searchElement(VisionProperties.Names.VOLUME, vminus$entity) != null)
            cir.setReturnValue(Math.max(0f, VisionProperties.getNumber(VisionProperties.Names.VOLUME, vminus$entity, cir.getReturnValue()).floatValue()));


    }

    @Inject(method = "getExperienceReward", at = @At("HEAD"), cancellable = true)
    private void getExperienceReward(CallbackInfoReturnable<Integer> cir) {
          /*
        if (VisionProperties.searchElement(VisionProperties.Names.XP, vminus$entity) != null)
            cir.setReturnValue(Math.max(0, VisionProperties.getNumber(VisionProperties.Names.XP, vminus$entity, cir.getReturnValue()).intValue()));


    }
    */

}
