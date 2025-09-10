package net.lixir.vminus.api.block.extensions;

import net.minecraft.client.player.Input;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public interface VMinusBlockExtensions {
    default @Nullable Input onPlayerInput(@NotNull Level level, @NotNull Player player, @NotNull Input input) {
        return null;
    }

    default int getWorldGenScheduledTick(Level level, BlockState state, BlockPos pos) {
        return -1;
    }
}
