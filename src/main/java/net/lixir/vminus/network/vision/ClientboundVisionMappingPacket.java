package net.lixir.vminus.network.vision;

import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ClientboundVisionMappingPacket {
    private final String visionTypeId;
    private final Map<ResourceLocation, Integer> mappings;

    public ClientboundVisionMappingPacket(String visionTypeId, Map<ResourceLocation, Integer> mappings) {
        this.visionTypeId = visionTypeId;
        this.mappings = mappings;
    }

    public static void encode(@NotNull ClientboundVisionMappingPacket msg, @NotNull FriendlyByteBuf buf) {
        buf.writeUtf(msg.visionTypeId);
        buf.writeVarInt(msg.mappings.size());
        for (var entry : msg.mappings.entrySet()) {
            buf.writeResourceLocation(entry.getKey());
            buf.writeVarInt(entry.getValue());
        }
    }

    @Contract("_ -> new")
    public static @NotNull ClientboundVisionMappingPacket decode(@NotNull FriendlyByteBuf buf) {
        String type = buf.readUtf();
        int size = buf.readVarInt();
        Map<ResourceLocation, Integer> mappings = new HashMap<>();
        for (int i = 0; i < size; i++) {
            mappings.put(buf.readResourceLocation(), buf.readVarInt());
        }
        return new ClientboundVisionMappingPacket(type, mappings);
    }

    public static void handle(ClientboundVisionMappingPacket msg, @NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            VisionType visionType = VisionTypes.get(msg.visionTypeId);
            if (visionType == null) return;

            for (Map.Entry<ResourceLocation, Integer> entry : msg.mappings.entrySet()) {
                ResourceLocation id = entry.getKey();
                int index = entry.getValue();

                Object target = visionType.registryGetter().apply(id);
                if (target != null) {
                    visionType.visionSetter().accept(target, index);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
