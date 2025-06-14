package net.lixir.vminus.events;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.network.VMinusNetworking;
import net.lixir.vminus.network.vision.VisionControlPacket;
import net.lixir.vminus.network.vision.VisionSyncPacket;
import net.lixir.vminus.util.VNBTUtils;
import net.lixir.vminus.visions.ItemVision;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class OnDatapackSyncEventHandler {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        /*
        List<ServerPlayer> players = new ArrayList<>();
        ServerPlayer checkPlayer = event.getPlayer();

        return;

        if (checkPlayer != null) {
            players.add(checkPlayer);

            if (!checkPlayer.server.isDedicatedServer())
                return;
        } else
            players.addAll(event.getPlayers());



        for (ServerPlayer serverPlayer : players) {
            VMinusNetworking.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new VisionControlPacket(VisionControlPacket.VisionControlType.CLEAR)
            );

            for (Item item : ForgeRegistries.ITEMS) {
                ItemVision vision = ItemVision.of(item);
                if (vision.isEmpty())
                    continue;
                JsonObject serialized = vision.serialize();


                if (serialized != null) {
                    String itemId = ForgeRegistries.ITEMS.getKey(item).toString();

                    JsonObject wrapper = new JsonObject();
                    wrapper.add("data", serialized);

                    JsonArray idArray = new JsonArray();
                    idArray.add(itemId);
                    wrapper.add("itemIds", idArray);

                    CompoundTag tag = VNBTUtils.jsonToCompound(wrapper);
                    VMinus.LOGGER.info("CompoundTag Output={}, Size={}", tag, tag.sizeInBytes());
                    VisionSyncPacket packet = new VisionSyncPacket(tag);
                    sendVisionDataToClient(serverPlayer, packet);
                }

            }

            VMinusNetworking.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new VisionControlPacket(VisionControlPacket.VisionControlType.FREEZE)
            );
        }

         */
    }

    private static void sendVisionDataToClient(ServerPlayer serverPlayer, VisionSyncPacket packet) {
        VMinusNetworking.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> serverPlayer),
                packet
        );
    }
}