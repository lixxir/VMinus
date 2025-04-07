package net.lixir.vminus.mixins.level;

import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.BlockVision;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunkSection.class)
public abstract class LevelChunkSectionMixin {
    @Unique
    private final LevelChunkSection vminus$levelChunkSection = (LevelChunkSection) (Object) this;

    @Shadow
    public abstract BlockState setBlockState(int x, int y, int z, BlockState state);

    @Inject(method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("HEAD"), cancellable = true)
    private void setBlockState(int x, int y, int z, BlockState blockState, CallbackInfoReturnable<BlockState> cir) {
        Block block = blockState.getBlock();
        BlockVision blockVision = BlockVision.of(block);
        Boolean ban = blockVision.ban.value(new VisionConditionArguments(blockState));
        Block replace = blockVision.replace.value(new VisionConditionArguments(blockState));
        if (replace != null && !replace.equals(block)) {
            cir.setReturnValue(setBlockState(x, y, z, replace.defaultBlockState()));
        } else if (ban != null && ban && !block.equals(Blocks.AIR)) {
            cir.setReturnValue(setBlockState(x, y, z, Blocks.AIR.defaultBlockState()));
        }

        /*

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

         */
    }
}
