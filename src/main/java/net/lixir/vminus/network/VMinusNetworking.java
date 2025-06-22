package net.lixir.vminus.network;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.network.vision.ClientboundVisionListPacket;
import net.lixir.vminus.network.vision.ClientboundVisionMappingPacket;
import net.lixir.vminus.network.vision.ClientboundDataResetPacket;
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

    private static int MESSAGE_ID = 0;

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        addNetworkMessage(
                VariantSyncPacket.class,
                VariantSyncPacket::encode,
                VariantSyncPacket::new,
                VariantSyncPacket::handle);
        addNetworkMessage(
                SyncCapePacket.class,
                SyncCapePacket::encode,
                SyncCapePacket::decode,
                SyncCapePacket::handle);
        addNetworkMessage(ClientboundSightSyncPacket.class,
                ClientboundSightSyncPacket::encode,
                ClientboundSightSyncPacket::decode,
                ClientboundSightSyncPacket::handle);
        addNetworkMessage(ClientboundDataResetPacket.class,
                ClientboundDataResetPacket::encode,
                ClientboundDataResetPacket::decode,
                ClientboundDataResetPacket::handle);
        addNetworkMessage(ClientboundVisionListPacket.class,
                ClientboundVisionListPacket::encode,
                ClientboundVisionListPacket::decode,
                ClientboundVisionListPacket::handle);
        addNetworkMessage(ClientboundVisionMappingPacket.class,
                ClientboundVisionMappingPacket::encode,
                ClientboundVisionMappingPacket::decode,
                ClientboundVisionMappingPacket::handle);
    }


    public static <T> void addNetworkMessage(Class<T> messageType,
                                             BiConsumer<T, FriendlyByteBuf> encoder,
                                             Function<FriendlyByteBuf, T> decoder,
                                             BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        CHANNEL.registerMessage(MESSAGE_ID, messageType, encoder, decoder, messageConsumer);
        MESSAGE_ID++;
    }

}
