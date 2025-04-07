package net.lixir.vminus.visions.resources.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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

import java.util.Collection;
import java.util.List;

public class VisionCreativeOrderParser extends AbstractVisionParser<VisionCreativeOrder> {
    @Override
    public @Nullable List<VisionValue<VisionCreativeOrder>> parse(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            ResourceLocation targetItemResourceLocation = arrayObject.has("target") ? parseResourceLocation("target", key, arrayObject) : null;
            ItemStack targetItemStack = null;
            if (targetItemResourceLocation != null) {
                Item targetItem = ForgeRegistries.ITEMS.getValue(targetItemResourceLocation);
                if (targetItem == null)
                    throw new JsonParseException(targetItemResourceLocation + " is not a valid target item for " + key + ".");
                targetItemStack = targetItem.getDefaultInstance();
            }

            String itemValue = arrayObject.getAsJsonPrimitive("item").getAsString();
            Boolean before = arrayObject.has("before") && arrayObject.get("before").isJsonPrimitive() && arrayObject.getAsJsonPrimitive("before").getAsBoolean();

            if (itemValue.startsWith("#")) {
                ResourceLocation tagLocation = new ResourceLocation(itemValue.substring(1));
                TagKey<Item> itemTag = TagKey.create(Registries.ITEM, tagLocation);
                Collection<Holder<Item>> tagItems = context.getTag(itemTag);

                if (tagItems.isEmpty()) {
                    throw new JsonParseException(tagLocation + " is not a valid tag or contains no items for " + key + ".");
                }

                for (Holder<Item> tagItemHolder : tagItems) {
                    Item tagItem = tagItemHolder.value();
                    ItemStack tagItemStack = tagItem.getDefaultInstance();
                    VisionCreativeOrder visionCreativeOrder = new VisionCreativeOrder(tagItemStack, targetItemStack, before);
                    visionValues.add(VisionValue.create(visionCreativeOrder, arrayObject, jsonObject, key));
                }
            }  else {
                ResourceLocation itemResourceLocation = new ResourceLocation(itemValue);
                Item item = ForgeRegistries.ITEMS.getValue(itemResourceLocation);
                if (item == null)
                    throw new JsonParseException(itemResourceLocation + " is not a valid item for " + key + ".");
                ItemStack itemStack = item.getDefaultInstance();

                VisionCreativeOrder visionCreativeOrder = new VisionCreativeOrder(itemStack, targetItemStack, before);
                visionValues.add(VisionValue.create(visionCreativeOrder, arrayObject, jsonObject, key));
            }
        }
        return visionValues;
    }
}
