package net.lixir.vminus.vision.resource.codec;

import com.google.gson.*;
import net.lixir.vminus.vision.values.VisionProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionDoubleCodec extends VisionCodec<Double> {
    @Override
    public @Nullable List<VisionProperty<Double>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<Double>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            double value = arrayObject.getAsJsonPrimitive("value").getAsDouble();

            visionProperties.add(VisionProperty.create(value, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(Double value) {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(value);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("value", jsonPrimitive);
        return jsonObject;
    }
}
