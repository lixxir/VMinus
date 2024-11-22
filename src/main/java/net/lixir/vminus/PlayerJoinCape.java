package net.lixir.vminus;

import net.lixir.vminus.network.VminusModVariables;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerJoinCape {
    @SubscribeEvent
    public static void onPlayerJoin(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer newPlayer = (ServerPlayer) event.getEntity();
        for (ServerPlayer otherPlayer : newPlayer.server.getPlayerList().getPlayers()) {
            otherPlayer.getCapability(VminusModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                VMinusMod.PACKET_HANDLER.sendTo(new SetCapePacket(capability.cape_id, otherPlayer.getUUID()), newPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            });
        }
    }
}
