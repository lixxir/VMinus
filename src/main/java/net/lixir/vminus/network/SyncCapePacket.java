package net.lixir.vminus.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Supplier;

public record SyncCapePacket(UUID playerUUID, String capeId) {
    public static void encode(@NotNull SyncCapePacket msg, @NotNull FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerUUID);
        buf.writeUtf(msg.capeId);
    }

    public static @NotNull SyncCapePacket decode(@NotNull FriendlyByteBuf buf) {
        UUID playerUUID = buf.readUUID();
        String capeId = buf.readUtf();
        return new SyncCapePacket(playerUUID, capeId);
    }

    public static void handle(SyncCapePacket msg, @NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                SyncCapePacketHandler.handle(msg);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void sendToAll(@NotNull ServerPlayer sourcePlayer, String capeId) {
        SyncCapePacket packet = new SyncCapePacket(sourcePlayer.getUUID(), capeId);
        VMinusNetworking.CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }
}
