package net.lixir.vminus.mixins.blocks;

import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.BlockEntryAccessor;
import net.lixir.vminus.registry.entry.RegistryEntryDefaults;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.BlockVision;
import net.lixir.vminus.visions.accessors.BlockVisionAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockRealMixin implements BlockVisionAccessor, BlockEntryAccessor, RegistryEntryDefaults<BlockEntry> {
    @Unique
    private final Block vminus$block = (Block) (Object) this;

    @Unique
    private BlockVision vminus$blockVision = null;

    @Unique
    private BlockEntry vminus$blockEntry = null;

    @Inject(method = "getSpeedFactor", at = @At("RETURN"), cancellable = true)
    private void getSpeedFactor(CallbackInfoReturnable<Float> cir) {
        Float value = vminus$getVision().speed_boost.value(new VisionConditionArguments(vminus$block));
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getFriction", at = @At("RETURN"), cancellable = true)
    private void getFriction(CallbackInfoReturnable<Float> cir) {
        Float value = vminus$getVision().friction.value(new VisionConditionArguments(vminus$block));
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getJumpFactor", at = @At("RETURN"), cancellable = true)
    private void getJumpFactor(CallbackInfoReturnable<Float> cir) {
        Float value = vminus$getVision().jump_boost.value(new VisionConditionArguments(vminus$block));
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getExplosionResistance", at = @At("RETURN"), cancellable = true)
    private void getExplosionResistance(CallbackInfoReturnable<Float> cir) {
        Float value = vminus$getVision().blast_resistance.value(new VisionConditionArguments(vminus$block));
        if (value != null) cir.setReturnValue(value);
    }
    @Inject(method = "getSoundType", at = @At("RETURN"), cancellable = true)
    private void getSoundType(BlockState state, CallbackInfoReturnable<SoundType> cir) {
        SoundType original = cir.getReturnValue();
        SoundType override = vminus$getVision().sound.value(new VisionConditionArguments(vminus$block));

        if (override != null) {
            float volume = override.getVolume() != 0.0F ? override.getVolume() : original.getVolume();
            float pitch = override.getPitch() != 0.0F ? override.getPitch() : original.getPitch();

            SoundEvent breakSound = override.getBreakSound() != null ? override.getBreakSound() : original.getBreakSound();
            SoundEvent stepSound  = override.getStepSound() != null  ? override.getStepSound()  : original.getStepSound();
            SoundEvent placeSound = override.getPlaceSound() != null ? override.getPlaceSound() : original.getPlaceSound();
            SoundEvent hitSound   = override.getHitSound() != null   ? override.getHitSound()   : original.getHitSound();
            SoundEvent fallSound  = override.getFallSound() != null  ? override.getFallSound()  : original.getFallSound();

            cir.setReturnValue(new SoundType(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound));
        }
    }


    @Override
    public @NotNull BlockVision vminus$getVision() {
        if (vminus$blockVision == null)
            return BlockVision.EMPTY;
        return vminus$blockVision;
    }

    @Override
    public void vminus$mergeVision(BlockVision vision) {
        if (vminus$blockVision == null)
            this.vminus$blockVision = vision;
        else
            this.vminus$blockVision.merge(vision);
    }

    @Override
    public void vminus$clearVision() {
        if (vminus$blockVision != null)
            this.vminus$blockVision = new BlockVision();
    }

    @Override
    public void vminus$freezeVision() {
        if (vminus$blockVision != null)
            this.vminus$blockVision.freeze();
    }

    @Override
    public void vminus$setEntry(BlockEntry blockEntry) {
        this.vminus$blockEntry = blockEntry;
    }

    @Override
    public BlockEntry vminus$getEntry() {
        return vminus$blockEntry;
    }
}
