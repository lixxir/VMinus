package net.lixir.vminus.network.resource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;

import java.io.IOException;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SendJsonFilesPacketHandler {
    private static final StringBuilder itemJsonAccumulator = new StringBuilder();
    private static final StringBuilder blockJsonAccumulator = new StringBuilder();
    private static final StringBuilder entityJsonAccumulator = new StringBuilder();
    private static final StringBuilder effectJsonAccumulator = new StringBuilder();
    private static final StringBuilder enchantmentJsonAccumulator = new StringBuilder();

    public static void handle(SendJsonFilesPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (packet.type() == VisionType.ITEM.getId()) {
                itemJsonAccumulator.append(packet.itemJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(itemJsonAccumulator.toString(), VisionType.ITEM.getId());
                    itemJsonAccumulator.setLength(0);
                }
            } else if (packet.type() == VisionType.BLOCK.getId()) {
                blockJsonAccumulator.append(packet.blockJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(blockJsonAccumulator.toString(), VisionType.BLOCK.getId());
                    blockJsonAccumulator.setLength(0);
                }
            } else if (packet.type() == VisionType.ENTITY.getId()) {
                entityJsonAccumulator.append(packet.entityJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(entityJsonAccumulator.toString(), VisionType.ENTITY.getId());
                    blockJsonAccumulator.setLength(0);
                }
            } else if (packet.type() == VisionType.EFFECT.getId()) {
                effectJsonAccumulator.append(packet.effectJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(effectJsonAccumulator.toString(), VisionType.EFFECT.getId());
                    effectJsonAccumulator.setLength(0);
                }
            } else if (packet.type() == VisionType.ENCHANTMENT.getId()) {
                enchantmentJsonAccumulator.append(packet.enchantmentJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(enchantmentJsonAccumulator.toString(),VisionType.ENCHANTMENT.getId());
                    enchantmentJsonAccumulator.setLength(0);
                }
            }
        });
        context.setPacketHandled(true);
    }

    private static void processReceivedJson(String jsonData, byte type) {
        try {
            JsonObject jsonObject = new Gson().fromJson(jsonData, JsonObject.class);
            if (jsonObject == null || jsonObject.isJsonNull())
                throw new IOException("Invalid JSON received.");
            switch (type) {
                case 0: {
                    VminusModVariables.main_item_vision = jsonObject;
                    break;
                }
                case 1: {
                    VminusModVariables.main_block_vision = jsonObject;
                    break;
                }
                case 2: {
                    VminusModVariables.main_entity_vision = jsonObject;
                    break;
                }
                case 3: {
                    VminusModVariables.main_effect_vision = jsonObject;
                    break;
                }
                case 4: {
                    VminusModVariables.main_enchantment_vision = jsonObject;
                    break;
                }
            }
        } catch (IOException e) {
            VMinusMod.LOGGER.error("Error processing received JSON: ", e);
        }
    }
}
