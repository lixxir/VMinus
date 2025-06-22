package net.lixir.vminus.events;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.network.ClientboundSightSyncPacket;
import net.lixir.vminus.network.VMinusNetworking;
import net.lixir.vminus.network.vision.ClientboundVisionListPacket;
import net.lixir.vminus.network.vision.ClientboundVisionMappingPacket;
import net.lixir.vminus.network.vision.ClientboundDataResetPacket;
import net.lixir.vminus.sight.resource.SightManager;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.resource.managers.VisionManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class OnDatapackSyncEventHandler {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        List<ServerPlayer> players = new ArrayList<>();
        ServerPlayer dedicatedPlayer = event.getPlayer();

        if (dedicatedPlayer != null) {
            players.add(dedicatedPlayer);

            if (!dedicatedPlayer.server.isDedicatedServer())
                return;
        } else
            players.addAll(event.getPlayers());

        for (ServerPlayer player : players) {
            VMinusNetworking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                    new ClientboundDataResetPacket());

            VMinusNetworking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                    new ClientboundSightSyncPacket(SightManager.getAllSights()));

            for (VisionManager<?> visionManager : VisionManager.getVisionManagers()) {
                VisionType visionType = visionManager.getVisionType();

                VMinusNetworking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                        new ClientboundVisionListPacket(
                                visionType.id(),
                                Vision.getAllVisions().stream()
                                        .map(Vision::toNbt)
                                        .filter(Objects::nonNull)
                                        .toList()
                        ));

                VMinusNetworking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                        new ClientboundVisionMappingPacket(
                                visionType.id(),
                                visionManager.getVisionIndexes()
                        ));
            }
        }
    }
}