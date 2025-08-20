package net.lixir.vminus.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record RequestCapesPacket() {
    @Contract("_ -> new")
    public static @NotNull RequestCapesPacket decode(FriendlyByteBuf buf) {
        return new RequestCapesPacket();
    }

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(RequestCapesPacket msg, @NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null)
                return;


            for (ServerPlayer online : sender.server.getPlayerList().getPlayers()) {
                online.getCapability(VMinusSavedData.PLAYER_VARIABLES_CAPABILITY).ifPresent(cap -> {
                    SyncCapePacket.sendTo(sender, online.getUUID(), cap.cape_id);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
