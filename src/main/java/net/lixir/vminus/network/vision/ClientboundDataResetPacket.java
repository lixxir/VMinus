package net.lixir.vminus.network.vision;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.vision.Vision;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ClientboundDataResetPacket {
    public ClientboundDataResetPacket() {
    }

    public static void encode(ClientboundDataResetPacket msg, FriendlyByteBuf buf) {
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ClientboundDataResetPacket decode(FriendlyByteBuf buf) {
        return new ClientboundDataResetPacket();
    }

    public static void handle(ClientboundDataResetPacket msg, @NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                Vision.resetVisions();
            } catch (Exception e) {
                VMinus.LOGGER.error("Failed to handle VisionControlPacket: {}", msg, e);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
