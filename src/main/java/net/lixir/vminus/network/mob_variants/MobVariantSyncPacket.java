package net.lixir.vminus.network.mob_variants;

import net.lixir.vminus.VMinusMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobVariantSyncPacket {
    private final int entityId;
    private final String variant;

    public MobVariantSyncPacket(int entityId, String variant) {
        this.entityId = entityId;
        this.variant = variant;
    }

    public static void encode(MobVariantSyncPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeUtf(packet.variant);
    }

    public static MobVariantSyncPacket decode(FriendlyByteBuf buffer) {
        return new MobVariantSyncPacket(buffer.readInt(), buffer.readUtf(32767));
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        VMinusMod.addNetworkMessage(
                MobVariantSyncPacket.class,
                MobVariantSyncPacket::encode,
                MobVariantSyncPacket::decode,
                MobVariantSyncPacketHandler::handle
        );
    }

    public int getEntityId() {
        return entityId;
    }

    public String getVariant() {
        return variant;
    }
}