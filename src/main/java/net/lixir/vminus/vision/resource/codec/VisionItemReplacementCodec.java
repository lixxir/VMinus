package net.lixir.vminus.vision.resource.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.vision.util.VisionItemReplacement;
import net.lixir.vminus.vision.values.VisionProperty;
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

public class VisionItemReplacementCodec extends VisionCodec<VisionItemReplacement> {
    @Override
    public Class<VisionItemReplacement> getClassType() {
        return VisionItemReplacement.class;
    }

    @Override
    public @Nullable List<VisionProperty<VisionItemReplacement>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<VisionItemReplacement>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            if (!arrayObject.has("value"))
                continue;
            String value = arrayObject.getAsJsonPrimitive("value").getAsString();

            if (value.startsWith("#")) {
                ResourceLocation tagLocation = new ResourceLocation(value.substring(1));
                TagKey<Item> tagKey = TagKey.create(Registries.ITEM, tagLocation);
                VisionItemReplacement visionItemReplacement = new VisionItemReplacement(null, tagKey);
                visionProperties.add(VisionProperty.create(visionItemReplacement, arrayObject, jsonObject, key));
            } else {
                ResourceLocation resourceLocation = parseResourceLocation(key, arrayObject);
                Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
                if (item == null)
                    throw new JsonParseException(resourceLocation + " is not a valid item.");
                ItemStack itemStack = item.getDefaultInstance();
                VisionItemReplacement visionItemReplacement = new VisionItemReplacement(itemStack, null);
                visionProperties.add(VisionProperty.create(visionItemReplacement, arrayObject, jsonObject, key));
            }
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull VisionItemReplacement value) {
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
