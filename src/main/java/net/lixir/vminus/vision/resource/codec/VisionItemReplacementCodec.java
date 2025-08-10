package net.lixir.vminus.vision.resource.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.vision.util.ItemReplacement;
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

public class VisionItemReplacementCodec extends VisionCodec<ItemReplacement> {
    @Override
    public Class<ItemReplacement> getClassType() {
        return ItemReplacement.class;
    }

    @Override
    public @Nullable List<VisionValue<ItemReplacement>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionValue<ItemReplacement>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            String value = arrayObject.getAsJsonPrimitive("value").getAsString();

            if (value.startsWith("#")) {
                ResourceLocation tagLocation = new ResourceLocation(value.substring(1));
                TagKey<Item> tagKey = TagKey.create(Registries.ITEM, tagLocation);
                ItemReplacement itemReplacement = new ItemReplacement(null, tagKey);
                visionProperties.add(VisionValue.create(itemReplacement, arrayObject, jsonObject, key));
            } else {
                ResourceLocation resourceLocation = parseResourceLocation(key, arrayObject);
                Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
                if (item == null)
                    throw new JsonParseException(resourceLocation + " is not a valid item.");
                ItemStack itemStack = item.getDefaultInstance();
                ItemReplacement itemReplacement = new ItemReplacement(itemStack, null);
                visionProperties.add(VisionValue.create(itemReplacement, arrayObject, jsonObject, key));
            }
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull ItemReplacement value) {
        JsonObject jsonObject = new JsonObject();
        if (value.tag() != null) {
            jsonObject.addProperty("value", "#" + value.tag().location());
        } else {

            ItemStack itemStack = value.itemStack();
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
            if (id == null)
                throw new JsonParseException("Cannot encode ItemStack with unregistered item: " + itemStack);

            jsonObject.addProperty("value", id.toString());
        }

        return jsonObject;
    }

}
