package net.lixir.vminus.network.mobvariants;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.util.EntityVariantUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public record RequestVariantTexturePacket(int entityId) {
    public static void encode(RequestVariantTexturePacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
    }

    public static RequestVariantTexturePacket decode(FriendlyByteBuf buffer) {
        return new RequestVariantTexturePacket(buffer.readInt());
    }

    public static void handle(RequestVariantTexturePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ServerLevel level = contextSupplier.get().getSender().serverLevel();
            Entity entity = level.getEntity(packet.entityId());
            if (entity != null) {
                String texture = EntityVariantUtil.getVariantTexture(entity);
                VMinus.PACKET_HANDLER.send(
                        PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                        new SyncVariantTexturePacket(packet.entityId(), texture)
                );
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
