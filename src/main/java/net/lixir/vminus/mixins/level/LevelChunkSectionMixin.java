package net.lixir.vminus.mixins.level;

import com.google.gson.JsonObject;
import net.lixir.vminus.util.DirectionHelper;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.util.VisionValueHandler;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunkSection.class)
public abstract class LevelChunkSectionMixin {
    @Unique
    private final LevelChunkSection vminus$levelChunkSection = (LevelChunkSection) (Object) this;

    @Inject(method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("HEAD"), cancellable = true)
    private void setBlockState(int x, int y, int z, BlockState state, boolean flag, CallbackInfoReturnable<BlockState> cir) {
        Block block = state.getBlock();
        JsonObject visionData = Vision.getData(block);
        // Replace takes priority over banning.
        if (visionData != null) {
            if (visionData.has("replace")) {
                String replaceString = VisionValueHandler.getFirstValidString(visionData, "replace", block);
                if (replaceString == null)
                    return;
                ResourceLocation replaceResourceLocation = new ResourceLocation(replaceString);
                Block replacingBlock = ForgeRegistries.BLOCKS.getValue(replaceResourceLocation);
                if (replacingBlock != null) {
                    cir.setReturnValue(vminus$levelChunkSection.setBlockState(x, y, z, replacingBlock.defaultBlockState(), flag));
                }
            } else if (visionData.has("banned")) {
                boolean banned = VisionValueHandler.isBooleanMet(visionData, "banned", block);
                if (banned) {
                    cir.cancel();
                    return;
                }
            }
            if (visionData.has("constant_direction")) {
                String directionString = VisionValueHandler.getFirstValidString(visionData, "constant_direction", block);
                if (directionString == null)
                    return;
                Direction direction = DirectionHelper.getDirectionFromString(directionString);
                if (direction != null) {
                    BlockState updatedState = DirectionHelper.applyDirectionToBlockState(state, direction);
                    if (updatedState != null && !state.equals(updatedState)) {
                        cir.setReturnValue(vminus$levelChunkSection.setBlockState(x, y, z, updatedState, flag));
                    }
                }
            }
        }
    }
}
