package net.lixir.vminus.mixins.level;

import com.google.gson.JsonObject;
import net.lixir.vminus.helpers.DirectionHelper;
import net.lixir.vminus.visions.VisionValueHelper;
import net.lixir.vminus.visions.VisionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class LevelMixin {
    @Shadow
    public abstract boolean setBlock(BlockPos pos, BlockState state, int i);

    @Shadow
    public abstract boolean setBlockAndUpdate(BlockPos pos, BlockState state);

    @Shadow
    public abstract void updateNeighborsAt(BlockPos pos, Block block);

    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", at = @At("HEAD"), cancellable = true)
    private void setBlock(BlockPos pos, BlockState state, int i, CallbackInfoReturnable<Boolean> cir) {
        Block block = state.getBlock();
        JsonObject visionData = VisionHandler.getVisionData(block);
        if (visionData != null && visionData.has("replace")) {
            String replaceString = VisionValueHelper.getFirstValidString(visionData, "replace", block);
            ResourceLocation resourceLocation = new ResourceLocation(replaceString);
            Block replacingBlock = ForgeRegistries.BLOCKS.getValue(resourceLocation);
            if (replacingBlock != null) {
                LevelAccessor world = (LevelAccessor) this;
                cir.setReturnValue(setBlock(pos, replacingBlock.defaultBlockState(), i));
                world.scheduleTick(pos, replacingBlock, 1);
            }
        } else if (visionData != null && visionData.has("banned")) {
            boolean banned = VisionValueHelper.isBooleanMet(visionData, "banned", block);
            if (banned)
                cir.cancel();
        }
        if (visionData != null && visionData.has("constant_direction")) {
            String directionString = VisionValueHelper.getFirstValidString(visionData, "constant_direction", block);
            Direction direction = DirectionHelper.getDirectionFromString(directionString);
            if (direction != null) {
                BlockState currentState = state;
                BlockState updatedState = DirectionHelper.applyDirectionToBlockState(currentState, direction);
                if (updatedState != null && !currentState.equals(updatedState)) {
                    cir.setReturnValue(setBlock(pos, updatedState, i));
                }
            }
        }
    }
}
