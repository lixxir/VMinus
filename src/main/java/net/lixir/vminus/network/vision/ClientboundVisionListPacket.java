package net.lixir.vminus.network.vision;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ClientboundVisionListPacket {
    private final String visionTypeId;
    private final List<CompoundTag> visions;

    public ClientboundVisionListPacket(String visionTypeId, List<CompoundTag> visions) {
        this.visionTypeId = visionTypeId;
        this.visions = visions;
    }

    public static void encode(@NotNull ClientboundVisionListPacket msg, @NotNull FriendlyByteBuf buf) {
        buf.writeUtf(msg.visionTypeId);
        buf.writeVarInt(msg.visions.size());
        for (CompoundTag tag : msg.visions) {
            buf.writeNbt(tag);
        }
    }

    @Contract("_ -> new")
    public static @NotNull ClientboundVisionListPacket decode(@NotNull FriendlyByteBuf buf) {
        String id = buf.readUtf();
        int size = buf.readVarInt();
        List<CompoundTag> visions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            visions.add(buf.readNbt());
        }
        return new ClientboundVisionListPacket(id, visions);
    }

    public static void handle(ClientboundVisionListPacket msg, @NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            VisionType type = VisionTypes.get(msg.visionTypeId);
            if (type == null) return;

            for (CompoundTag tag : msg.visions) {
                Vision vision = Vision.fromNbt(tag, type);
                Vision.getOrAddVisionIndex(vision);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
