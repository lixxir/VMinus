package net.lixir.vminus.vision.resource.codec;

import com.google.gson.*;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.vision.values.VisionProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionIntegerCodec extends VisionCodec<Integer> {
    @Override
    public Class<Integer> getClassType() {
        return Integer.class;
    }

    @Override
    public @Nullable List<VisionProperty<Integer>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<Integer>> visionProperties = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            int value;
            try {
                value = arrayObject.getAsJsonPrimitive("value").getAsInt();
            } catch (Exception e) {
                VMinus.LOGGER.error(jsonObject);
                throw new JsonParseException(e);
            }

            visionProperties.add(VisionProperty.create(value, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull Integer value) {
        return encodeInteger(value);
    }
}
