package net.lixir.vminus.mixins.level;

import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Level.class)
public abstract class LevelMixin {
    /*
    @Unique
    private final Level vminus$level = (Level) (Object) this;

    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", at = @At("HEAD"), cancellable = true)
    private void setBlock(BlockPos pos, BlockState state, int i, CallbackInfoReturnable<Boolean> cir) {
        Block block = state.getBlock();
        JsonObject visionData = Vision.getData(block);
        if (visionData != null) {
            if (visionData.has("replace")) {
                String replaceString = VisionValueHandler.getFirstValidString(visionData, "replace", block);
                if (replaceString != null) {
                    ResourceLocation resourceLocation = new ResourceLocation(replaceString);
                    Block replacingBlock = ForgeRegistries.BLOCKS.getValue(resourceLocation);
                    if (replacingBlock != null) {
                        LevelAccessor world = (LevelAccessor) this;
                        cir.setReturnValue(vminus$level.setBlock(pos, replacingBlock.defaultBlockState(), i));
                        world.scheduleTick(pos, replacingBlock, 1);
                    }
                }
            } else if (visionData.has("banned")) {
                boolean banned = VisionValueHandler.isBooleanMet(visionData, "banned", block);
                if (banned) {
                    cir.cancel();
                    return;
                }
            }
            if (visionData.has("constant_direction")){
                String directionString = VisionValueHandler.getFirstValidString(visionData, "constant_direction", block);
                if (directionString != null) {
                    Direction direction = DirectionHelper.getDirectionFromString(directionString);
                    if (direction != null) {
                        BlockState updatedState = DirectionHelper.applyDirectionToBlockState(state, direction);
                        if (updatedState != null && !state.equals(updatedState)) {
                            cir.setReturnValue(vminus$level.setBlock(pos, updatedState, i));
                        }
                    }
                }
            }
        }
    }

     */
}
