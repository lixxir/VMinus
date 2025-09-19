package net.lixir.vminus.mixins.world;

import net.lixir.vminus.api.block.extensions.VMinusBlockExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldGenRegion.class)
public class WorldGenRegionMixin {
    @Shadow @Final private ServerLevel level;

    @Inject(method = "setBlock", at = @At("TAIL"))
    private void vMinus$setBlock(BlockPos pos, @NotNull BlockState state, int flags, int recursionLimit, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof VMinusBlockExtensions VMinusBlockExtensions) {
            int tick = VMinusBlockExtensions.getWorldGenScheduledTick(level, state, pos);
            if (tick <= 0)
                return;
            ((WorldGenLevel) this).scheduleTick(pos, state.getBlock(), tick);
        }
    }
}
