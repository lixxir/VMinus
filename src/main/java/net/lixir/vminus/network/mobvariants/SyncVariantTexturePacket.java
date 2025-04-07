package net.lixir.vminus.network.mobvariants;

import net.lixir.vminus.util.IEntityVariantAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncVariantTexturePacket(int entityId, String texture) {
    public static void encode(SyncVariantTexturePacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeUtf(packet.texture);
    }

    public static SyncVariantTexturePacket decode(FriendlyByteBuf buffer) {
        int entityId = buffer.readInt();
        String texture = buffer.readUtf();
        return new SyncVariantTexturePacket(entityId, texture);
    }

    public static void handle(SyncVariantTexturePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                Entity entity = mc.level.getEntity(packet.entityId());
                if (entity instanceof LivingEntity livingEntity && !(entity instanceof Player)) {
                    IEntityVariantAccessor entityVariantAccessor = ((IEntityVariantAccessor) livingEntity);
                    entityVariantAccessor.vminus$setVariantTexture(new ResourceLocation(packet.texture));

                }
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
