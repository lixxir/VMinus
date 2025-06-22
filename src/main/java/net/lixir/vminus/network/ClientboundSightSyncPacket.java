package net.lixir.vminus.network;

import net.lixir.vminus.sight.resource.SightManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record ClientboundSightSyncPacket(Map<String, Boolean> sights) {
    public static void encode(@NotNull ClientboundSightSyncPacket msg, @NotNull FriendlyByteBuf buf) {
        buf.writeVarInt(msg.sights.size());
        for (Map.Entry<String, Boolean> entry : msg.sights.entrySet()) {
            buf.writeUtf(entry.getKey());
            buf.writeBoolean(entry.getValue());
        }
    }

    @Contract("_ -> new")
    public static @NotNull ClientboundSightSyncPacket decode(@NotNull FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Map<String, Boolean> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String key = buf.readUtf();
            boolean value = buf.readBoolean();
            map.put(key, value);
        }
        return new ClientboundSightSyncPacket(map);
    }

    public static void handle(ClientboundSightSyncPacket msg, @NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> SightManager.setAll(msg.sights));
        ctx.get().setPacketHandled(true);
    }
}
