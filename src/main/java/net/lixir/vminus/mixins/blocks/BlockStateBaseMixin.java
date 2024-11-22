package net.lixir.vminus.mixins.blocks;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionValueHelper;
import net.lixir.vminus.visions.VisionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    @Unique
    private final BlockState state = (BlockState) (Object) this;

    @Inject(method = "getLightEmission", at = @At("HEAD"), cancellable = true)
    private void getLightEmission(CallbackInfoReturnable<Integer> cir) {
        Block block = state.getBlock();
        JsonObject blockData = VisionHandler.getVisionData(block);
        if (blockData != null && blockData.has("light_level")) {
            int lightLevel = VisionValueHelper.isNumberMet(blockData, "light_level", (cir.getReturnValue() != null ? cir.getReturnValue() : 0), block);
            cir.setReturnValue(Math.min(Math.max(lightLevel, 0), 15));
        }
    }

    @Inject(method = "emissiveRendering", at = @At("HEAD"), cancellable = true)
    private void emissiveRendering(CallbackInfoReturnable<Boolean> cir) {
        Block block = state.getBlock();
        JsonObject blockData = VisionHandler.getVisionData(block);
        if (blockData != null && blockData.has("emissive_rendering")) {
            cir.setReturnValue(VisionValueHelper.isBooleanMet(blockData, "emissive_rendering", block));
        }
    }

    @Inject(method = "canOcclude", at = @At("HEAD"), cancellable = true)
    private void canOcclude(CallbackInfoReturnable<Boolean> cir) {
        Block block = state.getBlock();
        JsonObject blockData = VisionHandler.getVisionData(block);
        if (blockData != null && blockData.has("can_occlude")) {
            cir.setReturnValue(VisionValueHelper.isBooleanMet(blockData, "can_occlude", block));
        }
    }

    @Inject(method = "isRedstoneConductor", at = @At("HEAD"), cancellable = true)
    private void isRedstoneConductor(CallbackInfoReturnable<Boolean> cir) {
        Block block = state.getBlock();
        JsonObject blockData = VisionHandler.getVisionData(block);
        if (blockData != null && blockData.has("redstone_conductor")) {
            cir.setReturnValue(VisionValueHelper.isBooleanMet(blockData, "redstone_conductor", block));
        }
    }

    @Inject(method = "getDestroySpeed", at = @At("HEAD"), cancellable = true)
    private void getDestroySpeed(BlockGetter p_60801_, BlockPos p_60802_, CallbackInfoReturnable<Float> cir) {
        Block block = state.getBlock();
        JsonObject blockData = VisionHandler.getVisionData(block);
        if (blockData != null && blockData.has("destroy_time")) {
            float destroyTime = VisionValueHelper.isNumberMet(blockData, "destroy_time", (cir.getReturnValue() != null ? cir.getReturnValue() : 0), block);
            cir.setReturnValue(Math.max(-1, destroyTime));
        }
    }

    @Inject(method = "isValidSpawn", at = @At("HEAD"), cancellable = true)
    private void isValidSpawn(CallbackInfoReturnable<Boolean> cir) {
        BlockState state = (BlockState) (Object) this;
        if (state.is(BlockTags.create(new ResourceLocation("vminus:override_valid_spawning")))) {
            cir.setReturnValue(true);
        }
    }
}
