package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.EntityVision;
import net.lixir.vminus.visions.accessors.IEntityVisionAccessor;
import net.lixir.vminus.network.mobvariants.RequestVariantTexturePacket;
import net.lixir.vminus.registry.VMinusAttributes;
import net.lixir.vminus.util.IEntityVariantAccessor;
import net.lixir.vminus.util.ISpeedGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements ISpeedGetter, IEntityVisionAccessor, IEntityVariantAccessor {
    @Unique
    private final Entity vminus$entity = (Entity) (Object) this;

    @Unique
    private double vminus$speed = 0.0;

    @Unique
    private ResourceLocation vminus$variantTexture = null;

    @Unique
    private boolean vminus$hasRequestedVariant = false;


    @Inject(method = "setOldPosAndRot", at = @At("HEAD"))
    private void vminus$setOldPosAndRot(CallbackInfo ci) {
        var self = (Entity) (Object) this;
        var deltaX = self.getX() - self.xOld;
        var deltaZ = self.getZ() - self.zOld;
        vminus$speed = deltaX * deltaX + deltaZ * deltaZ;
    }



    @Override
    public void vminus$setVariantTexture(ResourceLocation texture) {
        this.vminus$variantTexture = texture;
    }



    @Override
    public ResourceLocation vminus$getVariantTexture() {
        return vminus$variantTexture;
    }

    @Unique
    public double vminus$getSpeed() {
        return vminus$speed;
    }

    @Unique
    private EntityVision vminus$entityVision = new EntityVision();

    @Inject(method = "<init>" , at = @At(value = "TAIL"))
    public void init(CallbackInfo ci) {
        /*
        if (vminus$entity != null && vminus$entity.level().isClientSide() && vminus$variantTexture == null) {
            VMinus.PACKET_HANDLER.sendToServer(new RequestVariantTexturePacket(vminus$entity.getId()));
        }

         */
    }

    @Inject(method = "baseTick", at = @At(value = "TAIL"))
    public void baseTick(CallbackInfo ci) {
        // vminus$entity.refreshDimensions();
        /*
        if (vminus$entity.level() instanceof ServerLevel)
        serverLevel.getServer().execute(() -> VMinus.PACKET_HANDLER.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                new MobVariantSyncPacket(entity.getId(), chosenVariant)
        ));

         */

        if (!vminus$hasRequestedVariant && vminus$entity != null && vminus$entity.level().isClientSide() && vminus$variantTexture == null) {
            vminus$hasRequestedVariant = true;
            VMinus.PACKET_HANDLER.sendToServer(new RequestVariantTexturePacket(vminus$entity.getId()));
        }
    }

    @Inject(method = "getEyeY", at = @At("RETURN"), cancellable = true)
    public void getEyeY(CallbackInfoReturnable<Double> cir) {
       // cir.setReturnValue(cir.getReturnValue() + 2);
    }

    @Inject(method = "isSilent", at = @At("RETURN"), cancellable = true)
    private void isSilent(CallbackInfoReturnable<Boolean> cir) {
        float translucency = vminus$entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*2f;
        if (translucency >= 1f)
            cir.setReturnValue(true);
        Boolean value = vminus$getVision().silent.value(new VisionConditionArguments.Builder().passEntity(vminus$entity).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "dampensVibrations", at = @At("RETURN"), cancellable = true)
    private void dampensVibrations(CallbackInfoReturnable<Boolean> cir) {
        float translucency = vminus$entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*1.5f;
        if (translucency >= 1f)
            cir.setReturnValue(true);
        Boolean value = vminus$getVision().dampens_vibration.value(new VisionConditionArguments.Builder().passEntity(vminus$entity).build());
        if (value != null) cir.setReturnValue(value);
    }


    @Inject(method = "playStepSound", at = @At("RETURN"), cancellable = true)
    private void vminus$playStepSound(BlockPos p_20135_, BlockState p_20136_, CallbackInfo ci) {
        float translucency = vminus$entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*2f;
        if (translucency >= 1f)
            ci.cancel();
    }

    @Inject(method = "playSwimSound", at = @At("RETURN"), cancellable = true)
    private void vminus$playSwimSound(float p_20213_, CallbackInfo ci) {
        float translucency = vminus$entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*2f;
        if (translucency >= 1f)
            ci.cancel();
    }

    @Inject(method = "isInvisible", at = @At("RETURN"), cancellable = true)
    public void isInvisible(CallbackInfoReturnable<Boolean> cir) {
        float translucency = vminus$entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*1.5f;
        if (translucency >= 1f)
            cir.setReturnValue(true);
    }

    @ModifyArg(
            method = "playStepSound",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
            ),
            index = 1
    )
    private float vminus$playStepSound(float originalVolume) {
        float translucency = vminus$entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*2f;
        return originalVolume * (1.0F - translucency);
    }

    @ModifyArg(
            method = "playSwimSound",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
            ),
            index = 1
    )
    protected float vminus$playSwimSound(float originalVolume) {
        float translucency = vminus$entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*2f;
        return originalVolume * (1.0F - translucency);
    }


    @Override
    public void vminus$setVision(EntityVision vision) {
        this.vminus$entityVision = vision;
    }

    @Override
    public EntityVision vminus$getVision() {
        if (vminus$entity.getType() instanceof IEntityVisionAccessor entityVisionable) {
            return entityVisionable.vminus$getVision();
        }
        return this.vminus$entityVision;
    }
}
