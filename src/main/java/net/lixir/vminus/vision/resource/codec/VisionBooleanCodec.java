package net.lixir.vminus.vision.resource.codec;

import com.google.gson.*;
import net.lixir.vminus.vision.values.VisionProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionBooleanCodec extends VisionCodec<Boolean> {
    @Override
    public Class<Boolean> getClassType() {
        return Boolean.class;
    }


    @Override
    public @Nullable List<VisionProperty<Boolean>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<Boolean>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            boolean value = arrayObject.getAsJsonPrimitive("value").getAsBoolean();

            visionProperties.add(VisionProperty.create(value, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull Boolean value) {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(value);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("value", jsonPrimitive);
        return jsonObject;
    }
}
