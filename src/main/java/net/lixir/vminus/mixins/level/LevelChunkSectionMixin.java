package net.lixir.vminus.mixins.level;

import com.google.gson.JsonObject;
import net.lixir.vminus.helpers.DirectionHelper;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHandler;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunkSection.class)
public abstract class LevelChunkSectionMixin {
    @Shadow
    public abstract BlockState setBlockState(int x, int y, int z, BlockState state, boolean flag);

    @Inject(method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("HEAD"), cancellable = true)
    private void setBlockState(int x, int y, int z, BlockState state, boolean flag, CallbackInfoReturnable<BlockState> cir) {
        Block block = state.getBlock();
        JsonObject visionData = VisionHandler.getVisionData(block);
        // Replace takes priority over banning.
        if (visionData != null && visionData.has("replace")) {
            String decoString = VisionValueHandler.getFirstValidString(visionData, "replace", block);
            ResourceLocation resourceLocation = new ResourceLocation(decoString);
            Block replacingBlock = ForgeRegistries.BLOCKS.getValue(resourceLocation);
            if (replacingBlock != null) {
                cir.setReturnValue(setBlockState(x, y, z, replacingBlock.defaultBlockState(), flag));
            }
        } else if (visionData != null && visionData.has("banned")) {
            boolean banned = VisionValueHandler.isBooleanMet(visionData, "banned", block);
            if (banned)
                cir.cancel();
        }
        if (visionData != null && visionData.has("constant_direction")) {
            String directionString = VisionValueHandler.getFirstValidString(visionData, "constant_direction", block);
            Direction direction = DirectionHelper.getDirectionFromString(directionString);
            if (direction != null) {
                BlockState currentState = state;
                BlockState updatedState = DirectionHelper.applyDirectionToBlockState(currentState, direction);
                if (updatedState != null && !currentState.equals(updatedState)) {
                    cir.setReturnValue(setBlockState(x, y, z, updatedState, flag));
                }
            }
        }
    }
}
