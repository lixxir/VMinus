package net.lixir.vminus.network.vision;

import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ClientboundVisionListPacket {

    private final String visionTypeId;
    private final List<Pair<List<ResourceLocation>, CompoundTag>> groupedVisions;

    public ClientboundVisionListPacket(String visionTypeId, List<Pair<List<ResourceLocation>, CompoundTag>> groupedVisions) {
        this.visionTypeId = visionTypeId;
        this.groupedVisions = groupedVisions;
    }

    public static void encode(@NotNull ClientboundVisionListPacket msg, @NotNull FriendlyByteBuf buf) {
        buf.writeUtf(msg.visionTypeId);
        buf.writeVarInt(msg.groupedVisions.size());

        for (Pair<List<ResourceLocation>, CompoundTag> pair : msg.groupedVisions) {
            List<ResourceLocation> ids = pair.getFirst();
            CompoundTag tag = pair.getSecond();

            buf.writeVarInt(ids.size());
            for (ResourceLocation id : ids) {
                buf.writeResourceLocation(id);
            }

            buf.writeNbt(tag);
        }
    }

    @Contract("_ -> new")
    public static @NotNull ClientboundVisionListPacket decode(@NotNull FriendlyByteBuf buf) {
        String id = buf.readUtf();
        int groupCount = buf.readVarInt();
        List<Pair<List<ResourceLocation>, CompoundTag>> grouped = new ArrayList<>();

        for (int i = 0; i < groupCount; i++) {
            int idCount = buf.readVarInt();
            List<ResourceLocation> ids = new ArrayList<>();
            for (int j = 0; j < idCount; j++) {
                ids.add(buf.readResourceLocation());
            }
            CompoundTag tag = buf.readNbt();
            grouped.add(Pair.of(ids, tag));
        }

        return new ClientboundVisionListPacket(id, grouped);
    }

    public static void handle(ClientboundVisionListPacket msg, @NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            VisionType<?> visionType = VisionTypes.get(msg.visionTypeId);
            if (visionType == null) {
                VMinus.LOGGER.warn("VisionType not found for id: {}", msg.visionTypeId);
                return;
            }

            for (int groupIndex = 0; groupIndex < msg.groupedVisions.size(); groupIndex++) {
                Pair<List<ResourceLocation>, CompoundTag> pair = msg.groupedVisions.get(groupIndex);
                List<ResourceLocation> ids = pair.getFirst();
                CompoundTag tag = pair.getSecond();

                for (ResourceLocation id : ids) {
                    try {
                        Vision.fromNbt(id, tag, visionType);
                        Object target = visionType.getRegistryGetter().apply(id);
                        if (target != null) {
                            visionType.getVisionSetter().accept(target, id);
                        }
                    } catch (Exception ex) {
                        VMinus.LOGGER.error("Error syncing vision for id {}: ", id, ex);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
