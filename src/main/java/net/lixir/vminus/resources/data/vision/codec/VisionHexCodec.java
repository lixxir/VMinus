package net.lixir.vminus.resources.data.vision.codec;

import com.google.gson.*;
import net.lixir.vminus.vision.values.VisionValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionHexCodec extends VisionCodec<Integer> {
    @Override
    public Class<Integer> getClassType() {
        return Integer.class;
    }

    @Override
    public @Nullable List<VisionValue<Integer>> decode(JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionValue<Integer>> visionProperties = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            String value = arrayObject.getAsJsonPrimitive("value").getAsString();
            if (value.startsWith("#")) {
                value = value.substring(1);
            }
            int colorInt = Integer.parseInt(value, 16);

            visionProperties.add(VisionValue.create(colorInt, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull Integer value) {
        return encodeInteger(value);
    }
}
