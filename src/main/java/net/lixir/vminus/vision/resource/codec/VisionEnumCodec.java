package net.lixir.vminus.vision.resource.codec;

import com.google.gson.*;
import com.google.gson.JsonParseException;
import net.lixir.vminus.vision.values.VisionProperty;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionEnumCodec<E extends Enum<E>> extends VisionCodec<E> {

    private final Class<E> enumType;

    public VisionEnumCodec(Class<E> enumType) {
        this.enumType = enumType;
    }

    @Override
    public @Nullable List<VisionProperty<E>> decode(JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<E>> visionProperties = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);

        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            String valueString = arrayObject.getAsJsonPrimitive("value").getAsString();
            E enumValue;

            try {
                enumValue = Enum.valueOf(enumType, valueString.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new JsonParseException(valueString + " is not a valid value for enum " + enumType.getSimpleName());
            }

            VisionProperty<E> visionProperty = VisionProperty.create(enumValue, arrayObject, jsonObject, key);
            visionProperties.add(visionProperty);
        }

        return visionProperties;
    }
}
