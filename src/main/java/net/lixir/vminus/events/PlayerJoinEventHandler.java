package net.lixir.vminus.events;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.network.capes.SetCapePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;

@Mod.EventBusSubscriber
public class PlayerJoinEventHandler {
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer serverPlayer = (ServerPlayer) event.getEntity();
        for (ServerPlayer otherPlayer : serverPlayer.server.getPlayerList().getPlayers()) {
            otherPlayer.getCapability(VminusModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                VMinusMod.PACKET_HANDLER.sendTo(new SetCapePacket(capability.cape_id, otherPlayer.getUUID()), serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            });
        }
    }
}
