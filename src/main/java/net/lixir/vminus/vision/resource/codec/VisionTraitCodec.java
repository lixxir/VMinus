package net.lixir.vminus.vision.resource.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.item.trait.ItemTraits;
import net.lixir.vminus.vision.util.VisionTrait;
import net.lixir.vminus.vision.values.VisionProperty;
import net.lixir.vminus.item.trait.ItemTrait;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionTraitCodec extends VisionCodec<VisionTrait> {
    @Override
    public Class<VisionTrait> getClassType() {
        return VisionTrait.class;
    }

    @Override
    public @Nullable List<VisionProperty<VisionTrait>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<VisionTrait>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            Boolean value = arrayObject.getAsJsonPrimitive("value").getAsBoolean();
            ResourceLocation resourceLocation = parseResourceLocation("id", key, arrayObject);
            ItemTrait itemTrait = ItemTraits.fromId(resourceLocation);
            VisionTrait visionTrait = new VisionTrait(itemTrait, value);

            visionProperties.add(VisionProperty.create(visionTrait, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull VisionTrait value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", value.itemTrait().getName());
        jsonObject.addProperty("value", value.value());
        return jsonObject;
    }
}
