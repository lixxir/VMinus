package net.lixir.vminus.vision.resource.codec;

import com.google.gson.*;
import net.lixir.vminus.vision.util.ItemStackWrapper;
import net.lixir.vminus.vision.values.VisionValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionItemStackCodec extends VisionCodec<ItemStackWrapper> {
    @Override
    public Class<ItemStackWrapper> getClassType() {
        return ItemStackWrapper.class;
    }

    @Override
    public @Nullable List<VisionValue<ItemStackWrapper>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionValue<ItemStackWrapper>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            ResourceLocation resourceLocation = parseResourceLocation(key, arrayObject);
            Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
            if (item == null)
                throw new JsonParseException(resourceLocation + " is not a valid item.");
            ItemStack itemStack = item.getDefaultInstance();
            ItemStackWrapper itemStackWrapper = new ItemStackWrapper(itemStack);
            visionProperties.add(VisionValue.create(itemStackWrapper, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull ItemStackWrapper value) {
        JsonObject jsonObject = new JsonObject();
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(value.itemStack().getItem());
        if (id == null)
            throw new JsonParseException("Cannot encode ItemStack with unregistered item: " + value);
        JsonPrimitive idPrimitive = new JsonPrimitive(id.toString());
        jsonObject.add("value", idPrimitive);
        return jsonObject;
    }
}
