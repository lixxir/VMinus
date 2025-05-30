package net.lixir.vminus.network.vision;

import com.google.gson.*;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.util.VNBTUtils;
import net.lixir.vminus.visions.ItemVision;
import net.lixir.vminus.visions.accessors.ItemVisionAccessor;
import net.lixir.vminus.visions.resources.VisionDeserializer;
import net.lixir.vminus.visions.resources.VisionProcessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class VisionSyncPacket {
    private static final Gson CLIENT_VISION_GSON = new GsonBuilder()
            .registerTypeAdapter(ItemVision.class, new VisionDeserializer<>(ItemVision.class, null))
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private final CompoundTag tag;

    public VisionSyncPacket(CompoundTag tag) {
        this.tag = tag;
    }

    public static void encode(VisionSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.tag);
    }

    public static VisionSyncPacket decode(FriendlyByteBuf buf) {
        return new VisionSyncPacket(buf.readNbt());
    }

    public static void handle(VisionSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            CompoundTag wrapper = msg.tag;
            CompoundTag visionTag = wrapper.getCompound("data");

            try {
                JsonObject wrapperJson = VNBTUtils.compoundToJson(wrapper);
                JsonObject packetJson = wrapperJson.getAsJsonObject("data");
                JsonArray itemIds = wrapperJson.getAsJsonArray("itemIds");

                JsonObject processed = VisionProcessor.processJson("visions/items", packetJson.deepCopy());

                ItemVision vision = CLIENT_VISION_GSON.fromJson(processed, ItemVision.class);

                for (JsonElement itemIdElement : itemIds) {
                    String itemId = itemIdElement.getAsString();
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
                    if (item != null) {
                        ((ItemVisionAccessor) item).vminus$mergeVision(vision);
                    }
                }
            } catch (Exception e) {
                VMinus.LOGGER.error("Failed to convert or deserialize vision tag: {}", visionTag, e);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
