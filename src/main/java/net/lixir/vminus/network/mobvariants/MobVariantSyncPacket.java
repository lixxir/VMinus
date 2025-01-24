package net.lixir.vminus.network.mobvariants;

import net.lixir.vminus.VMinus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public record MobVariantSyncPacket(int entityId, String variant) {

    public static void encode(MobVariantSyncPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeUtf(packet.variant);
    }

    public static MobVariantSyncPacket decode(FriendlyByteBuf buffer) {
        return new MobVariantSyncPacket(buffer.readInt(), buffer.readUtf(32767));
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        VMinus.addNetworkMessage(
                MobVariantSyncPacket.class,
                MobVariantSyncPacket::encode,
                MobVariantSyncPacket::decode,
                MobVariantSyncPacketHandler::handle
        );
    }
}