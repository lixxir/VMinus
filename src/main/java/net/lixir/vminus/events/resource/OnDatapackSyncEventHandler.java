package net.lixir.vminus.events.resource;

import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.network.ClientboundSightSyncPacket;
import net.lixir.vminus.network.VMinusNetwork;
import net.lixir.vminus.network.vision.ClientboundVisionListPacket;
import net.lixir.vminus.network.vision.ClientboundDataResetPacket;
import net.lixir.vminus.sight.resource.SightManager;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.resource.manager.VisionManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Mod.EventBusSubscriber
public class OnDatapackSyncEventHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onDatapackSync(@NotNull OnDatapackSyncEvent event) {
        List<ServerPlayer> players = new ArrayList<>();
        ServerPlayer dedicatedPlayer = event.getPlayer();

        if (dedicatedPlayer != null) {
            if (dedicatedPlayer.level().isClientSide)
                return;

            players.add(dedicatedPlayer);
            if (!dedicatedPlayer.server.isDedicatedServer())
                return;
        } else
            players.addAll(event.getPlayers());


        for (ServerPlayer player : players) {
            VMinusNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                    new ClientboundDataResetPacket());
            VMinusNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                    new ClientboundSightSyncPacket(SightManager.getAllSights()));

            for (VisionManager<?> visionManager : List.copyOf(VisionManager.getVisionManagers())) {
                VisionType<?> visionType = visionManager.getVisionType();


                Map<CompoundTag, List<ResourceLocation>> grouped = new HashMap<>();
                List<CompoundTag> existingTags = new ArrayList<>();

                for (Map.Entry<ResourceLocation, ? extends Vision> entry : List.copyOf(visionManager.getVisionEntries())) {
                    ResourceLocation id = entry.getKey();
                    Vision vision = entry.getValue();
                    CompoundTag tag = vision.encode(visionType);


                    CompoundTag match = null;
                    for (CompoundTag existing : existingTags) {
                        if (existing.equals(tag)) {
                            match = existing;
                            break;
                        }
                    }

                    if (match != null) {
                        grouped.get(match).add(id);
                    } else {
                        grouped.put(tag, new ArrayList<>(List.of(id)));
                        existingTags.add(tag);
                    }
                }

                List<Pair<List<ResourceLocation>, CompoundTag>> groupedPairs = new ArrayList<>();
                for (Map.Entry<CompoundTag, List<ResourceLocation>> entry : grouped.entrySet()) {
                    groupedPairs.add(Pair.of(entry.getValue(), entry.getKey()));
                }

                List<List<Pair<List<ResourceLocation>, CompoundTag>>> chunks = splitGrouped(groupedPairs);
                int i = 0;
                for (List<Pair<List<ResourceLocation>, CompoundTag>> chunk : chunks) {
                    ClientboundVisionListPacket packet = new ClientboundVisionListPacket(
                            visionType.getId(), chunk
                    );
                    VMinusNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
                    i++;
                }
            }
        }
    }

    public static final int MAX_PACKET_SIZE = 30_000;

    private static @NotNull List<List<Pair<List<ResourceLocation>, CompoundTag>>> splitGrouped(@NotNull List<Pair<List<ResourceLocation>, CompoundTag>> all) {
        List<List<Pair<List<ResourceLocation>, CompoundTag>>> chunks = new ArrayList<>();
        List<Pair<List<ResourceLocation>, CompoundTag>> current = new ArrayList<>();
        int size = 0;

        for (var pair : all) {
            int entrySize = getGroupedVisionEntrySize(pair);

            if (size + entrySize > MAX_PACKET_SIZE && !current.isEmpty()) {
                chunks.add(current);
                current = new ArrayList<>();
                size = 0;
            }

            current.add(pair);
            size += entrySize;
        }

        if (!current.isEmpty()) {
            chunks.add(current);
        }
        return chunks;
    }

    private static int getGroupedVisionEntrySize(@NotNull Pair<List<ResourceLocation>, CompoundTag> pair) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            NbtIo.writeCompressed(pair.getSecond(), baos);
            baos.flush();
            int tagSize = baos.size();

            int idsSize = 0;

            idsSize += FriendlyByteBuf.getVarIntSize(pair.getFirst().size());

            for (ResourceLocation id : pair.getFirst()) {
                String full = id.toString();
                idsSize += FriendlyByteBuf.getVarIntSize(full.length());
                idsSize += full.getBytes(StandardCharsets.UTF_8).length;
            }

            return idsSize + tagSize;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
