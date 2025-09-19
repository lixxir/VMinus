package net.lixir.vminus.resources.data.vision.codec;

import com.google.gson.*;
import net.lixir.vminus.vision.values.VisionValue;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionResourceLocationCodec extends VisionCodec<ResourceLocation> {

    @Override
    public Class<ResourceLocation> getClassType() {
        return ResourceLocation.class;
    }

    @Override
    public @Nullable List<VisionValue<ResourceLocation>> decode(JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionValue<ResourceLocation>> visionProperties = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        if (jsonArray == null) return visionProperties;

        for (JsonElement element : jsonArray) {
            JsonObject arrayObject = element.getAsJsonObject();
            String valueString = arrayObject.getAsJsonPrimitive("value").getAsString();
            ResourceLocation value = new ResourceLocation(valueString);

            visionProperties.add(VisionValue.create(value, arrayObject, jsonObject, key));
        }

        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull ResourceLocation value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", value.toString());
        return jsonObject;
    }
}
