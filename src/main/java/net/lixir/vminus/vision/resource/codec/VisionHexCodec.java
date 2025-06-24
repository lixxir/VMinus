package net.lixir.vminus.vision.resource.codec;

import com.google.gson.*;
import net.lixir.vminus.vision.values.VisionProperty;
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
    public @Nullable List<VisionProperty<Integer>> decode(JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<Integer>> visionProperties = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            String value = arrayObject.getAsJsonPrimitive("value").getAsString();
            if (value.startsWith("#")) {
                value = value.substring(1);
            }
            int colorInt = Integer.parseInt(value, 16);

            visionProperties.add(VisionProperty.create(colorInt, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull Integer value) {
        return encodeInteger(value);
    }
}
