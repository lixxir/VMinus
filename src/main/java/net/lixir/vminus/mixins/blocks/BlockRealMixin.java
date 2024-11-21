package net.lixir.vminus.mixins.blocks;

import com.google.gson.JsonObject;
import net.lixir.vminus.VisionValueHelper;
import net.lixir.vminus.SoundHelper;
import net.lixir.vminus.VisionHandler;
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
    private final Block block = (Block) (Object) this;

    @Inject(method = "getSpeedFactor", at = @At("HEAD"), cancellable = true)
    private void getSpeedFactor(CallbackInfoReturnable<Float> cir) {
        JsonObject blockData = VisionHandler.getVisionData(block);
        if (blockData != null && blockData.has("speed_factor")) {
            cir.setReturnValue(VisionValueHelper.isNumberMet(blockData, "speed_factor", cir.getReturnValue() != null ? cir.getReturnValue() : 1.0F, block));
        }
    }

    @Inject(method = "getFriction", at = @At("HEAD"), cancellable = true)
    private void getFriction(CallbackInfoReturnable<Float> cir) {
        JsonObject blockData = VisionHandler.getVisionData(block);
        if (blockData != null && blockData.has("friction")) {
            cir.setReturnValue(VisionValueHelper.isNumberMet(blockData, "friction", cir.getReturnValue() != null ? cir.getReturnValue() : 1.0F, block));
        }
    }

    @Inject(method = "getJumpFactor", at = @At("HEAD"), cancellable = true)
    private void getJumpFactor(CallbackInfoReturnable<Float> cir) {
        JsonObject blockData = VisionHandler.getVisionData(block);
        if (blockData != null && blockData.has("jump_factor")) {
            cir.setReturnValue(VisionValueHelper.isNumberMet(blockData, "jump_factor", cir.getReturnValue() != null ? cir.getReturnValue() : 1.0F, block));
        }
    }

    @Inject(method = "getExplosionResistance", at = @At("HEAD"), cancellable = true)
    private void getExplosionResistance(CallbackInfoReturnable<Float> cir) {
        JsonObject blockData = VisionHandler.getVisionData(block);
        if (blockData != null && blockData.has("explosion_resistance")) {
            cir.setReturnValue(Math.max(VisionValueHelper.isNumberMet(blockData, "explosion_resistance", cir.getReturnValue() != null ? cir.getReturnValue() : 1.0F, block), -1));
        }
    }

    @Inject(method = "getSoundType", at = @At("HEAD"), cancellable = true)
    private void getSoundType(BlockState state, CallbackInfoReturnable<SoundType> cir) {
        JsonObject blockData = VisionHandler.getVisionData(block);
        if (blockData != null && blockData.has("sound")) {
            String breakSound = VisionValueHelper.getFirstValidString(blockData, "sound", block, "break");
            String stepSound = VisionValueHelper.getFirstValidString(blockData, "sound", block, "step");
            String placeSound = VisionValueHelper.getFirstValidString(blockData, "sound", block, "place");
            String hitSound = VisionValueHelper.getFirstValidString(blockData, "sound", block, "hit");
            String fallSound = VisionValueHelper.getFirstValidString(blockData, "sound", block, "fall");
            SoundType soundType = SoundHelper.CreateBlockSoundType(breakSound, stepSound, placeSound, hitSound, fallSound);
            if (soundType != null)
                cir.setReturnValue(soundType);
        }
    }
}
