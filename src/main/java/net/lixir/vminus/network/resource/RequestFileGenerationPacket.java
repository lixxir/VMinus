package net.lixir.vminus.network.resource;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.visions.ResourceVisionLoader;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RequestFileGenerationPacket {
    public RequestFileGenerationPacket() {
    }

    public static void handle(RequestFileGenerationPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        ServerPlayer player = contextSupplier.get().getSender();
        contextSupplier.get().enqueueWork(() -> {
            LevelAccessor world = player.level;
            if (VisionType.ITEM.getMainVision().entrySet().isEmpty())
                ResourceVisionLoader.generateItemVisionsFile(world);
            if (VisionType.BLOCK.getMainVision().entrySet().isEmpty())
                ResourceVisionLoader.generateBlockVisionsFile(world);
            if (VisionType.ENTITY.getMainVision().entrySet().isEmpty())
                ResourceVisionLoader.generateEntityVisionsFile(world);
            if (VisionType.EFFECT.getMainVision().entrySet().isEmpty())
                ResourceVisionLoader.generateEffectVisionsFile(world);
            if (VisionType.ENCHANTMENT.getMainVision().entrySet().isEmpty())
                ResourceVisionLoader.generateEnchantmentVisionsFile(world);
            sendFilesToClient(player);
        });
        contextSupplier.get().setPacketHandled(true);
    }

    private static void sendFilesToClient(ServerPlayer player) {
        String itemJson = VisionType.ITEM.getMainVision().toString();
        String blockJson = VisionType.BLOCK.getMainVision().toString();
        String entityJson = VisionType.ENTITY.getMainVision().toString();
        String effectJson = VisionType.EFFECT.getMainVision().toString();
        String enchantmentJson = VisionType.ENCHANTMENT.getMainVision().toString();
        sendJsonInChunks(player, itemJson, blockJson, entityJson, effectJson, enchantmentJson);
    }

    private static void sendJsonInChunks(ServerPlayer player, String itemJson, String blockJson, String entityJson, String effectJson, String enchantmentJson) {
        int chunkSize = 32767;
        for (int i = 0; i < itemJson.length(); i += chunkSize) {
            String chunk = itemJson.substring(i, Math.min(itemJson.length(), i + chunkSize));
            boolean isLastChunk = (i + chunkSize) >= itemJson.length();
            VMinusMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new SendJsonFilesPacket(chunk, "", "", "", "", isLastChunk, VisionType.ITEM.getId()));
        }
        for (int i = 0; i < blockJson.length(); i += chunkSize) {
            String chunk = blockJson.substring(i, Math.min(blockJson.length(), i + chunkSize));
            boolean isLastChunk = (i + chunkSize) >= blockJson.length();
            VMinusMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new SendJsonFilesPacket("", chunk, "", "", "", isLastChunk, VisionType.BLOCK.getId()));
        }
        for (int i = 0; i < entityJson.length(); i += chunkSize) {
            String chunk = entityJson.substring(i, Math.min(entityJson.length(), i + chunkSize));
            boolean isLastChunk = (i + chunkSize) >= entityJson.length();
            VMinusMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new SendJsonFilesPacket("", "", chunk, "", "", isLastChunk, VisionType.ENTITY.getId()));
        }
        for (int i = 0; i < effectJson.length(); i += chunkSize) {
            String chunk = effectJson.substring(i, Math.min(effectJson.length(), i + chunkSize));
            boolean isLastChunk = (i + chunkSize) >= entityJson.length();
            VMinusMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new SendJsonFilesPacket("", "", "", chunk, "", isLastChunk, VisionType.EFFECT.getId()));
        }
        for (int i = 0; i < enchantmentJson.length(); i += chunkSize) {
            String chunk = enchantmentJson.substring(i, Math.min(enchantmentJson.length(), i + chunkSize));
            boolean isLastChunk = (i + chunkSize) >= enchantmentJson.length();
            VMinusMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new SendJsonFilesPacket("", "", "", "", chunk, isLastChunk, VisionType.ENCHANTMENT.getId()));
        }
    }

    public static void encode(RequestFileGenerationPacket message, FriendlyByteBuf buffer) {
    }

    public static RequestFileGenerationPacket decode(FriendlyByteBuf buffer) {
        return new RequestFileGenerationPacket();
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        VMinusMod.addNetworkMessage(RequestFileGenerationPacket.class, RequestFileGenerationPacket::encode, RequestFileGenerationPacket::decode, RequestFileGenerationPacket::handle);
    }
}
