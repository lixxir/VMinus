package net.lixir.vminus.mixins.blocks;

import com.google.gson.JsonObject;
import net.lixir.vminus.SoundHelper;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Block.class)
public abstract class BlockRealMixin {
    @Unique
    private final Block vminus$block = (Block) (Object) this;

    @Inject(method = "getSpeedFactor", at = @At("RETURN"), cancellable = true)
    private void getSpeedFactor(CallbackInfoReturnable<Float> cir) {
        if (VisionProperties.findSearchObject(VisionProperties.Names.SPEED_FACTOR, vminus$block) != null)
            cir.setReturnValue(VisionProperties.getNumber(VisionProperties.Names.SPEED_FACTOR, vminus$block, cir.getReturnValue()).floatValue());
    }

    @Inject(method = "getFriction", at = @At("RETURN"), cancellable = true)
    private void getFriction(CallbackInfoReturnable<Float> cir) {
        if (VisionProperties.findSearchObject(VisionProperties.Names.FRICTION, vminus$block) != null)
            cir.setReturnValue(VisionProperties.getNumber(VisionProperties.Names.FRICTION, vminus$block, cir.getReturnValue()).floatValue());
    }

    @Inject(method = "getJumpFactor", at = @At("RETURN"), cancellable = true)
    private void getJumpFactor(CallbackInfoReturnable<Float> cir) {
        if (VisionProperties.findSearchObject(VisionProperties.Names.JUMP_FACTOR, vminus$block) != null)
            cir.setReturnValue(VisionProperties.getNumber(VisionProperties.Names.JUMP_FACTOR, vminus$block, cir.getReturnValue()).floatValue());
    }

    @Inject(method = "getExplosionResistance", at = @At("RETURN"), cancellable = true)
    private void getExplosionResistance(CallbackInfoReturnable<Float> cir) {
        if (VisionProperties.findSearchObject(VisionProperties.Names.BLAST_RESISTANCE, vminus$block) != null)
            cir.setReturnValue(VisionProperties.getNumber(VisionProperties.Names.BLAST_RESISTANCE, vminus$block, cir.getReturnValue()).floatValue());
    }

    @Inject(method = "getSoundType", at = @At("RETURN"), cancellable = true)
    private void getSoundType(BlockState state, CallbackInfoReturnable<SoundType> cir) {
        JsonObject visionData = Vision.getData(vminus$block);
        if (visionData == null) return;

        JsonObject soundObject = VisionProperties.findSearchObject(VisionProperties.Names.SOUND, visionData);
        if (soundObject == null) return;

        String stateId = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(state.getBlock())).toString();

        if (Vision.BLOCK_SOUND_TYPE_CACHE.containsKey(stateId)) {
            cir.setReturnValue(Vision.BLOCK_SOUND_TYPE_CACHE.get(stateId));
            return;
        }

        String breakSound = VisionProperties.getString(soundObject, visionData, VisionProperties.Names.BREAK, vminus$block);
        String stepSound = VisionProperties.getString(soundObject, visionData, VisionProperties.Names.STEP, vminus$block);
        String placeSound = VisionProperties.getString(soundObject, visionData, VisionProperties.Names.PLACE, vminus$block);
        String hitSound = VisionProperties.getString(soundObject, visionData, VisionProperties.Names.HIT, vminus$block);
        String fallSound = VisionProperties.getString(soundObject, visionData, VisionProperties.Names.FALL, vminus$block);

        SoundType soundType = SoundHelper.CreateBlockSoundType(breakSound, stepSound, placeSound, hitSound, fallSound);

        Vision.BLOCK_SOUND_TYPE_CACHE.put(stateId, soundType);

        cir.setReturnValue(soundType);
    }

}
