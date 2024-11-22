package net.lixir.vminus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SendJsonFilesPacket {
    private final String itemJsonChunk;
    private final String blockJsonChunk;
    private final String entityJsonChunk;
    private final String effectJsonChunk;
    private final String enchantmentJsonChunk;
    private final boolean isLastChunk;
    private final byte type;

    public SendJsonFilesPacket(String itemJsonChunk, String blockJsonChunk, String entityJsonChunk, String effectJsonChunk, String enchantmentJsonChunk, boolean isLastChunk, byte type) {
        this.itemJsonChunk = itemJsonChunk;
        this.blockJsonChunk = blockJsonChunk;
        this.entityJsonChunk = entityJsonChunk;
        this.effectJsonChunk = effectJsonChunk;
        this.enchantmentJsonChunk = enchantmentJsonChunk;
        this.isLastChunk = isLastChunk;
        this.type = type;
    }

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

    public String getItemJsonChunk() {
        return itemJsonChunk;
    }

    public String getBlockJsonChunk() {
        return blockJsonChunk;
    }

    public String getEntityJsonChunk() {
        return entityJsonChunk;
    }

    public String getEffectJsonChunk() {
        return effectJsonChunk;
    }

    public String getEnchantmentJsonChunk() {
        return enchantmentJsonChunk;
    }

    public boolean isLastChunk() {
        return isLastChunk;
    }

    public byte getType() {
        return type;
    }
}
