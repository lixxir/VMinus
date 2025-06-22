package net.lixir.vminus.vision.resource.codec;

import com.google.gson.*;
import net.lixir.vminus.vision.values.VisionProperty;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionRandomIntCodec extends VisionCodec<Integer> {
    @Override
    public @Nullable List<VisionProperty<Integer>> decode(JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<Integer>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            if (!arrayObject.has("value"))
                throw new JsonParseException(jsonObject + " has no value");
            JsonElement jsonElement = arrayObject.get("value");
            int value = arrayObject.getAsJsonPrimitive("value").getAsInt();
            if (jsonElement.isJsonObject()) {

            } else  {

            }


            visionProperties.add(VisionProperty.create(value, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }
}
