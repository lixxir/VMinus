package net.lixir.vminus.resources.data.vision.codec;

import com.google.gson.*;
import net.lixir.vminus.vision.values.VisionValue;
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
    public @Nullable List<VisionValue<Boolean>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionValue<Boolean>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            boolean value = arrayObject.getAsJsonPrimitive("value").getAsBoolean();

            visionProperties.add(VisionValue.create(value, arrayObject, jsonObject, key));
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
