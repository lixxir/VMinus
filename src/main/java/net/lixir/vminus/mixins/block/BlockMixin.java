package net.lixir.vminus.mixins.block;

import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.BlockEntryAccessor;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionPropertyTypes;
import net.lixir.vminus.vision.util.VisionUtil;
import net.lixir.vminus.vision.values.conditions.VisionContext;
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
public class BlockMixin implements VisionDuck, BlockEntryAccessor {
    @Unique
    private final Block vMinus$self = (Block) (Object) this;

    @Unique
    private BlockEntry vMinus$blockEntry = null;

    @Unique
    private int vMinus$visionIndex = 0;

    @Inject(method = "getSpeedFactor", at = @At("RETURN"), cancellable = true)
    public final void vMinus$getSpeedFactor(CallbackInfoReturnable<Float> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Blocks.SPEED_FACTOR, new VisionContext(vMinus$self));
    }

    @Inject(method = "getFriction", at = @At("RETURN"), cancellable = true)
    public final void vMinus$getFriction(CallbackInfoReturnable<Float> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Blocks.FRICTION, new VisionContext(vMinus$self));
    }

    @Inject(method = "getJumpFactor", at = @At("RETURN"), cancellable = true)
    public final void vMinus$getJumpFactor(CallbackInfoReturnable<Float> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Blocks.JUMP_FACTOR, new VisionContext(vMinus$self));
    }

    @Inject(method = "getExplosionResistance", at = @At("RETURN"), cancellable = true)
    public final void vMinus$getExplosionResistance(CallbackInfoReturnable<Float> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Blocks.BLAST_RESISTANCE, new VisionContext(vMinus$self));
    }

    @SuppressWarnings("deprecation")
    @Inject(method = "getSoundType", at = @At("RETURN"), cancellable = true)
    public final void vMinus$getSoundType(BlockState state, @NotNull CallbackInfoReturnable<SoundType> cir) {
        SoundType original = cir.getReturnValue();
        SoundType override = VisionUtil.getOverrideValue(this, VisionPropertyTypes.Blocks.SOUND, new VisionContext(vMinus$self));

        if (override != null) {
            float volume = override.getVolume() != 0.0F ? override.getVolume() : original.getVolume();
            float pitch = override.getPitch() != 0.0F ? override.getPitch() : original.getPitch();
            cir.setReturnValue(new SoundType(volume, pitch, override.getBreakSound(), override.getStepSound(), override.getPlaceSound(), override.getHitSound(), override.getFallSound()));
        }
    }


    @Override
    public void vminus$setEntry(BlockEntry blockEntry) {
        this.vMinus$blockEntry = blockEntry;
    }

    @Override
    public BlockEntry vminus$getEntry() {
        return vMinus$blockEntry;
    }

    @Override
    public void vMinus$setVisionIndex(int index) {
        vMinus$visionIndex = index;
    }

    @Override
    public int vMinus$getVisionIndex() {
        return vMinus$visionIndex;
    }
}
