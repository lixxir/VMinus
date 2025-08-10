package net.lixir.vminus.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class SyncCapePacketHandler {
    public static void handle(@NotNull SyncCapePacket msg) {
        Player player = Minecraft.getInstance().level.getPlayerByUUID(msg.playerUUID());
        if (player != null) {
            player.getCapability(VMinusSavedData.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> capability.cape_id = msg.capeId());
        }
    }
}
