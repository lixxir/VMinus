package net.lixir.vminus.network;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.network.vision.VisionControlPacket;
import net.lixir.vminus.network.vision.VisionSyncPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VMinusNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(VMinus.ID, "network_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int messageID = 0;

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        addNetworkMessage(VariantSyncPacket.class, VariantSyncPacket::encode, VariantSyncPacket::new, VariantSyncPacket::handle);
        addNetworkMessage(VisionSyncPacket.class, VisionSyncPacket::encode, VisionSyncPacket::decode, VisionSyncPacket::handle);
        addNetworkMessage(VisionControlPacket.class, VisionControlPacket::encode, VisionControlPacket::decode, VisionControlPacket::handle);
    }

    public static <T> void addNetworkMessage(Class<T> messageType,
                                             BiConsumer<T, FriendlyByteBuf> encoder,
                                             Function<FriendlyByteBuf, T> decoder,
                                             BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        CHANNEL.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

}
