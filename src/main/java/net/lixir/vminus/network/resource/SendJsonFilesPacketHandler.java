package net.lixir.vminus.network.resource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.network.resource.SendJsonFilesPacket;
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
            if (packet.getType() == (byte) 0) {
                itemJsonAccumulator.append(packet.getItemJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(itemJsonAccumulator.toString(), (byte) 0);
                    itemJsonAccumulator.setLength(0);
                }
            } else if (packet.getType() == (byte) 1) {
                blockJsonAccumulator.append(packet.getBlockJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(blockJsonAccumulator.toString(), (byte) 1);
                    blockJsonAccumulator.setLength(0);
                }
            } else if (packet.getType() == (byte) 2) {
                entityJsonAccumulator.append(packet.getEntityJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(entityJsonAccumulator.toString(), (byte) 2);
                    blockJsonAccumulator.setLength(0);
                }
            } else if (packet.getType() == (byte) 3) {
                effectJsonAccumulator.append(packet.getEffectJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(effectJsonAccumulator.toString(), (byte) 3);
                    effectJsonAccumulator.setLength(0);
                }
            } else if (packet.getType() == (byte) 4) {
                enchantmentJsonAccumulator.append(packet.getEnchantmentJsonChunk());
                if (packet.isLastChunk()) {
                    processReceivedJson(enchantmentJsonAccumulator.toString(), (byte) 4);
                    enchantmentJsonAccumulator.setLength(0);
                }
            }
        });
        context.setPacketHandled(true);
    }

    private static void processReceivedJson(String jsonData, byte type) {
        try {
            JsonObject jsonObject = new Gson().fromJson(jsonData, JsonObject.class);
            if (jsonObject == null || jsonObject.isJsonNull()) {
                throw new IOException("Invalid JSON received.");
            }
            switch (type) {
                case 0:
                    VminusModVariables.main_item_vision = jsonObject;
                    VMinusMod.LOGGER.info("Processed Item Vision: " + jsonObject);
                    break;
                case 1:
                    VminusModVariables.main_block_vision = jsonObject;
                    VMinusMod.LOGGER.info("Processed Block Vision: " + jsonObject);
                    break;
                case 2:
                    VminusModVariables.main_entity_vision = jsonObject;
                    VMinusMod.LOGGER.info("Processed Entity Vision: " + jsonObject);
                    break;
                case 3:
                    VminusModVariables.main_effect_vision = jsonObject;
                    VMinusMod.LOGGER.info("Processed Effect Vision: " + jsonObject);
                    break;
                case 4:
                    VminusModVariables.main_enchantment_vision = jsonObject;
                    VMinusMod.LOGGER.info("Processed Encahntment Vision: " + jsonObject);
                    break;
            }
        } catch (IOException e) {
            VMinusMod.LOGGER.error("Error processing received JSON: ", e);
        }
    }
}
