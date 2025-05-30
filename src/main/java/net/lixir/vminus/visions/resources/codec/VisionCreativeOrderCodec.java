package net.lixir.vminus.visions.resources.codec;

import com.google.gson.*;
import net.lixir.vminus.visions.util.VisionCreativeOrder;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VisionCreativeOrderCodec extends AbstractVisionCodec<VisionCreativeOrder> {
    @Override
    public @Nullable List<VisionValue<VisionCreativeOrder>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<VisionCreativeOrder>> visionValues = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);

        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            String itemValue;
            String targetValue;
            Boolean before;

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
                    throw new JsonParseException("Invalid value format for " + key + ": " + value);
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

            if (itemValue.startsWith("#")) {
                ResourceLocation tagLoc = new ResourceLocation(itemValue.substring(1));
                TagKey<Item> itemTag = TagKey.create(Registries.ITEM, tagLoc);
                Collection<Holder<Item>> tagItems = context.getTag(itemTag);

                if (tagItems.isEmpty())
                    throw new JsonParseException(tagLoc + " is not a valid tag or contains no items for " + key + ".");

                for (Holder<Item> tagItemHolder : tagItems) {
                    Item tagItem = tagItemHolder.value();
                    ItemStack tagItemStack = tagItem.getDefaultInstance();
                    VisionCreativeOrder order = new VisionCreativeOrder(tagItemStack, targetItemStack, before);
                    visionValues.add(VisionValue.create(order, arrayObject, jsonObject, key));
                }
            } else {
                ResourceLocation itemLoc = new ResourceLocation(itemValue);
                Item item = ForgeRegistries.ITEMS.getValue(itemLoc);
                if (item == null)
                    throw new JsonParseException(itemLoc + " is not a valid item for " + key + ".");
                ItemStack itemStack = item.getDefaultInstance();

                VisionCreativeOrder order = new VisionCreativeOrder(itemStack, targetItemStack, before);
                visionValues.add(VisionValue.create(order, arrayObject, jsonObject, key));
            }
        }

        return visionValues;
    }

    @Override
    public @Nullable JsonObject encode(VisionCreativeOrder value) {
        JsonObject jsonObject = new JsonObject();

        if (value.getItemStack() != null) {
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(value.getItemStack().getItem());
            assert value.getTargetItemStack() != null;
            ResourceLocation targetId = ForgeRegistries.ITEMS.getKey(value.getTargetItemStack().getItem());

            if (itemId != null && targetId != null) {
                String arrow = value.isBefore() ? "<" : ">";
                jsonObject.addProperty("value", itemId + arrow + targetId);
                return jsonObject;
            }
        }

        if (value.getItemStack() != null) {
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(value.getItemStack().getItem());
            if (itemId != null)
                jsonObject.addProperty("item", itemId.toString());
        }
        if (value.getTargetItemStack() != null) {
            ResourceLocation targetId = ForgeRegistries.ITEMS.getKey(value.getTargetItemStack().getItem());
            if (targetId != null)
                jsonObject.addProperty("target", targetId.toString());
        }
        if (value.isBefore() != null) {
            jsonObject.addProperty("before", value.isBefore());
        }

        return jsonObject;
    }
}
