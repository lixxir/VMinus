package net.lixir.vminus.visions.resources.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.visions.util.VisionEntityVariant;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionItemStackCodec extends AbstractVisionCodec<ItemStack> {
    @Override
    public @Nullable List<VisionValue<ItemStack>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<ItemStack>> visionValues = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            ResourceLocation resourceLocation = parseResourceLocation(key, arrayObject);
            Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
            if (item == null)
                throw new JsonParseException(resourceLocation + " is not a valid item.");
            ItemStack itemStack = item.getDefaultInstance();

            visionValues.add(VisionValue.create(itemStack, arrayObject, jsonObject, key));
        }
        return visionValues;
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
