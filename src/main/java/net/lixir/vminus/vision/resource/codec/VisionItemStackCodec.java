package net.lixir.vminus.vision.resource.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.vision.values.VisionProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionItemStackCodec extends VisionCodec<ItemStack> {
    @Override
    public @Nullable List<VisionProperty<ItemStack>> decode(JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<ItemStack>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            ResourceLocation resourceLocation = parseResourceLocation(key, arrayObject);
            Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
            if (item == null)
                throw new JsonParseException(resourceLocation + " is not a valid item.");
            ItemStack itemStack = item.getDefaultInstance();

            visionProperties.add(VisionProperty.create(itemStack, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(ItemStack value) {
        if (value == null || value.isEmpty())
            return null;

        JsonObject jsonObject = new JsonObject();
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(value.getItem());
        if (id == null)
            throw new JsonParseException("Cannot encode ItemStack with unregistered item: " + value);

        jsonObject.addProperty("value", id.toString());
        return jsonObject;
    }
}
