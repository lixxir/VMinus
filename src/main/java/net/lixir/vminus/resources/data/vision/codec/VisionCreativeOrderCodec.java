package net.lixir.vminus.resources.data.vision.codec;

import com.google.gson.*;
import net.lixir.vminus.vision.util.CreativeOrder;
import net.lixir.vminus.vision.values.VisionValue;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionCreativeOrderCodec extends VisionCodec<CreativeOrder> {
    @Override
    public Class<CreativeOrder> getClassType() {
        return CreativeOrder.class;
    }

    @Override
    public @Nullable List<VisionValue<CreativeOrder>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionValue<CreativeOrder>> visionProperties = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);

        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            String itemValue;
            String targetValue;
            boolean before;

            if (arrayObject.has("value")) {
                String value = arrayObject.getAsJsonPrimitive("value").getAsString();
                if (value.contains(">")) {
                    String[] parts = value.split(">");
                    if (parts.length != 2)
                        throw new JsonParseException("Invalid value format for " + key + ": " + value);
                    itemValue = parts[0].trim();
                    targetValue = parts[1].trim();
                    before = false;
                } else if (value.contains("<")) {
                    String[] parts = value.split("<");
                    if (parts.length != 2)
                        throw new JsonParseException("Invalid value format for " + key + ": " + value);
                    itemValue = parts[0].trim();
                    targetValue = parts[1].trim();
                    before = true;
                } else {
                    itemValue = value;
                    targetValue = null;
                    before = false;
                }
            } else {
                itemValue = arrayObject.getAsJsonPrimitive("item").getAsString();
                targetValue = arrayObject.has("target") ? arrayObject.getAsJsonPrimitive("target").getAsString() : null;
                before = arrayObject.has("before") && arrayObject.get("before").isJsonPrimitive() && arrayObject.getAsJsonPrimitive("before").getAsBoolean();
            }

            ItemStack targetItemStack = null;
            if (targetValue != null) {
                ResourceLocation targetLoc = new ResourceLocation(targetValue);
                Item targetItem = ForgeRegistries.ITEMS.getValue(targetLoc);
                if (targetItem == null)
                    throw new JsonParseException(targetLoc + " is not a valid target item for " + key + ".");
                targetItemStack = targetItem.getDefaultInstance();
            }

            CreativeOrder order;
            if (itemValue.startsWith("#")) {
                ResourceLocation tagLoc = new ResourceLocation(itemValue.substring(1));
                TagKey<Item> itemTag = TagKey.create(Registries.ITEM, tagLoc);
                order = new CreativeOrder(null, targetItemStack, before, itemTag);
            } else {
                ResourceLocation itemLoc = new ResourceLocation(itemValue);
                Item item = ForgeRegistries.ITEMS.getValue(itemLoc);
                if (item == null)
                    throw new JsonParseException(itemLoc + " is not a valid item for " + key + ".");
                ItemStack itemStack = item.getDefaultInstance();


                order = new CreativeOrder(itemStack, targetItemStack, before, null);
            }
            visionProperties.add(VisionValue.create(order, arrayObject, jsonObject, key));
        }

        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull CreativeOrder value) {
        JsonObject jsonObject = new JsonObject();

        ResourceLocation itemId = value.getItemStack() != null ? ForgeRegistries.ITEMS.getKey(value.getItemStack().getItem()) : null;
        ResourceLocation targetId = value.getTargetItemStack() != null ? ForgeRegistries.ITEMS.getKey(value.getTargetItemStack().getItem()) : null;

        if (itemId != null && targetId != null) {
            String arrow = value.isBefore() ? "<" : ">";
            jsonObject.addProperty("value", itemId + arrow + targetId);
            return jsonObject;
        }

        if (value.getTagKey() != null) {
            jsonObject.addProperty("item", "#" + value.getTagKey().location().toString());
        } else if (itemId != null) {
            jsonObject.addProperty("item", itemId.toString());
        }

        if (targetId != null) {
            jsonObject.addProperty("target", targetId.toString());
        }

        jsonObject.addProperty("before", value.isBefore());
        return jsonObject;
    }
}
