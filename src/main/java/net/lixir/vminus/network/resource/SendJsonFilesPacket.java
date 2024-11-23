package net.lixir.vminus.network.resource;

import net.lixir.vminus.VMinusMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public record SendJsonFilesPacket(String itemJsonChunk, String blockJsonChunk, String entityJsonChunk,
                                  String effectJsonChunk, String enchantmentJsonChunk, boolean isLastChunk, byte type) {

    public static void encode(SendJsonFilesPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.itemJsonChunk);
        buffer.writeUtf(packet.blockJsonChunk);
        buffer.writeUtf(packet.entityJsonChunk);
        buffer.writeUtf(packet.effectJsonChunk);
        buffer.writeUtf(packet.enchantmentJsonChunk);
        buffer.writeBoolean(packet.isLastChunk);
        buffer.writeByte(packet.type);
    }

    public static SendJsonFilesPacket decode(FriendlyByteBuf buffer) {
        String itemJsonChunk = buffer.readUtf(32767);
        String blockJsonChunk = buffer.readUtf(32767);
        String entityJsonChunk = buffer.readUtf(32767);
        String effectJsonChunk = buffer.readUtf(32767);
        String enchantmentJsonChunk = buffer.readUtf(32767);
        boolean isLastChunk = buffer.readBoolean();
        byte type = buffer.readByte();
        return new SendJsonFilesPacket(itemJsonChunk, blockJsonChunk, entityJsonChunk, effectJsonChunk, enchantmentJsonChunk, isLastChunk, type);
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        VMinusMod.addNetworkMessage(SendJsonFilesPacket.class, SendJsonFilesPacket::encode, SendJsonFilesPacket::decode, SendJsonFilesPacketHandler::handle);
    }
}
