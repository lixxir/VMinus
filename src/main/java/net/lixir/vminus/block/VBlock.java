package net.lixir.vminus.block;

import net.minecraft.client.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public interface VBlock {
    default boolean shouldNaturallyScheduleTick() {
        return false;
    }

    default Input onPlayerInput(@NotNull Level level, @NotNull Player player, @NotNull Input input) {
        return input;
    }
}
