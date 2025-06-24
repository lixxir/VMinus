package net.lixir.vminus.vision.resource.codec;

import com.google.gson.*;
import net.lixir.vminus.vision.values.VisionProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionFloatCodec extends VisionCodec<Float> {
    @Override
    public Class<Float> getClassType() {
        return Float.class;
    }

    @Override
    public @Nullable List<VisionProperty<Float>> decode(JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<Float>> visionProperties = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            float value = arrayObject.getAsJsonPrimitive("value").getAsFloat();

            visionProperties.add(VisionProperty.create(value, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull Float value) {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(value);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("value", jsonPrimitive);
        return jsonObject;
    }
}
