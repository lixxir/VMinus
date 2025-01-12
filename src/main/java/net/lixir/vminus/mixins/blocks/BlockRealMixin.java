package net.lixir.vminus.mixins.blocks;

import com.google.gson.JsonObject;
import net.lixir.vminus.SoundHelper;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionValueHandler;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockRealMixin {
    @Unique
    private final Block vminus$block = (Block) (Object) this;


    @Inject(method = "getSpeedFactor", at = @At("RETURN"), cancellable = true)
    private void getSpeedFactor(CallbackInfoReturnable<Float> cir) {
        JsonObject blockData = VisionHandler.getVisionData(vminus$block);
        if (blockData != null && blockData.has("speed_factor")) {
            cir.setReturnValue(VisionValueHandler.isNumberMet(blockData, "speed_factor", cir.getReturnValue() != null ? cir.getReturnValue() : 1.0F, vminus$block));
        }
    }

    @Inject(method = "getFriction", at = @At("RETURN"), cancellable = true)
    private void getFriction(CallbackInfoReturnable<Float> cir) {
        JsonObject blockData = VisionHandler.getVisionData(vminus$block);
        if (blockData != null && blockData.has("friction")) {
            cir.setReturnValue(VisionValueHandler.isNumberMet(blockData, "friction", cir.getReturnValue() != null ? cir.getReturnValue() : 1.0F, vminus$block));
        }
    }

    @Inject(method = "getJumpFactor", at = @At("RETURN"), cancellable = true)
    private void getJumpFactor(CallbackInfoReturnable<Float> cir) {
        JsonObject blockData = VisionHandler.getVisionData(vminus$block);
        if (blockData != null && blockData.has("jump_factor")) {
            cir.setReturnValue(VisionValueHandler.isNumberMet(blockData, "jump_factor", cir.getReturnValue() != null ? cir.getReturnValue() : 1.0F, vminus$block));
        }
    }

    @Inject(method = "getExplosionResistance", at = @At("RETURN"), cancellable = true)
    private void getExplosionResistance(CallbackInfoReturnable<Float> cir) {
        JsonObject blockData = VisionHandler.getVisionData(vminus$block);
        if (blockData != null && blockData.has("explosion_resistance")) {
            cir.setReturnValue(Math.max(VisionValueHandler.isNumberMet(blockData, "explosion_resistance", cir.getReturnValue() != null ? cir.getReturnValue() : 1.0F, vminus$block), -1));
        }
    }

    @Inject(method = "getSoundType", at = @At("RETURN"), cancellable = true)
    private void getSoundType(BlockState state, CallbackInfoReturnable<SoundType> cir) {
        JsonObject blockData = VisionHandler.getVisionData(vminus$block);
        if (blockData != null && blockData.has("sound")) {
            String breakSound = VisionValueHandler.getFirstValidString(blockData, "sound", vminus$block, "break");
            String stepSound = VisionValueHandler.getFirstValidString(blockData, "sound", vminus$block, "step");
            String placeSound = VisionValueHandler.getFirstValidString(blockData, "sound", vminus$block, "place");
            String hitSound = VisionValueHandler.getFirstValidString(blockData, "sound", vminus$block, "hit");
            String fallSound = VisionValueHandler.getFirstValidString(blockData, "sound", vminus$block, "fall");
            SoundType soundType = SoundHelper.CreateBlockSoundType(breakSound, stepSound, placeSound, hitSound, fallSound);
            cir.setReturnValue(soundType);
        }
    }
}
