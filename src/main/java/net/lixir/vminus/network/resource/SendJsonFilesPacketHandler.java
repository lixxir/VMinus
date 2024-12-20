package net.lixir.vminus.network.resource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.visions.VisionHandler;
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
            if (packet.type() == VisionHandler.ITEM_TYPE) {
                itemJsonAccumulator.append(packet.itemJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(itemJsonAccumulator.toString(), VisionHandler.ITEM_TYPE);
                    itemJsonAccumulator.setLength(0);
                }
            } else if (packet.type() == VisionHandler.BLOCK_TYPE) {
                blockJsonAccumulator.append(packet.blockJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(blockJsonAccumulator.toString(), VisionHandler.BLOCK_TYPE);
                    blockJsonAccumulator.setLength(0);
                }
            } else if (packet.type() == VisionHandler.ENTITY_TYPE) {
                entityJsonAccumulator.append(packet.entityJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(entityJsonAccumulator.toString(), VisionHandler.ENTITY_TYPE);
                    blockJsonAccumulator.setLength(0);
                }
            } else if (packet.type() == VisionHandler.EFFECT_TYPE) {
                effectJsonAccumulator.append(packet.effectJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(effectJsonAccumulator.toString(), VisionHandler.EFFECT_TYPE);
                    effectJsonAccumulator.setLength(0);
                }
            } else if (packet.type() == VisionHandler.ENCHANTMENT_TYPE) {
                enchantmentJsonAccumulator.append(packet.enchantmentJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(enchantmentJsonAccumulator.toString(), VisionHandler.ENCHANTMENT_TYPE);
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
                case VisionHandler.ITEM_TYPE:
                    VminusModVariables.main_item_vision = jsonObject;
                    break;
                case VisionHandler.BLOCK_TYPE:
                    VminusModVariables.main_block_vision = jsonObject;
                    break;
                case VisionHandler.ENTITY_TYPE:
                    VminusModVariables.main_entity_vision = jsonObject;
                    break;
                case VisionHandler.EFFECT_TYPE:
                    VminusModVariables.main_effect_vision = jsonObject;
                    break;
                case VisionHandler.ENCHANTMENT_TYPE:
                    VminusModVariables.main_enchantment_vision = jsonObject;
                    break;
            }
        } catch (IOException e) {
            VMinusMod.LOGGER.error("Error processing received JSON: ", e);
        }
    }
}
