package net.lixir.vminus.network.capes;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetCapePacket {
    private final String capeId;
    private final UUID playerUUID;

    public SetCapePacket(String capeId, UUID playerUUID) {
        this.capeId = capeId;
        this.playerUUID = playerUUID;
    }

    public static void encode(SetCapePacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.capeId);
        buffer.writeUUID(packet.playerUUID);
    }

    public static SetCapePacket decode(FriendlyByteBuf buffer) {
        String capeId = buffer.readUtf(32767);
        UUID playerUUID = buffer.readUUID();
        return new SetCapePacket(capeId, playerUUID);
    }

    public static void handle(SetCapePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                AbstractClientPlayer clientPlayer = (AbstractClientPlayer) Minecraft.getInstance().level.getPlayerByUUID(packet.playerUUID);
                if (clientPlayer != null) {
                    clientPlayer.getCapability(VminusModVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(capability -> {
                        capability.cape_id = packet.capeId;
                    });
                }
            } else {
                ServerPlayer player = context.getSender();
                if (player != null) {
                    player.getCapability(VminusModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.cape_id = packet.capeId;
                        capability.syncPlayerVariables(player);
                        for (ServerPlayer otherPlayer : player.server.getPlayerList().getPlayers()) {
                            VMinusMod.PACKET_HANDLER.sendTo(new SetCapePacket(capability.cape_id, player.getUUID()), otherPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                        }
                    });
                } else {
                    VMinusMod.LOGGER.error("Could not find the player for the cape packet!");
                }
            }
        });
        context.setPacketHandled(true);
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        VMinusMod.addNetworkMessage(SetCapePacket.class, SetCapePacket::encode, SetCapePacket::decode, SetCapePacket::handle);
    }
}
