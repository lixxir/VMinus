package net.lixir.vminus.network.vision;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.visions.resources.VisionResourceController;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class VisionControlPacket {
    public enum VisionControlType {
        CLEAR, FREEZE
    }

    private final VisionControlType type;

    public VisionControlPacket(VisionControlType type) {
        this.type = type;
    }

    public static void encode(VisionControlPacket msg, FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", msg.type.name());
        buf.writeNbt(tag);
    }

    public static VisionControlPacket decode(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();
        VisionControlType type = VisionControlType.valueOf(tag.getString("type"));
        return new VisionControlPacket(type);
    }

    public static void handle(VisionControlPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                switch (msg.type) {
                    case CLEAR -> VisionResourceController.clearVisions();
                    case FREEZE -> VisionResourceController.freezeVisions();
                }
            } catch (Exception e) {
                VMinus.LOGGER.error("Failed to handle VisionControlPacket: {}", msg.type, e);
            }
        });
        ctx.get().setPacketHandled(true);
    }


}
