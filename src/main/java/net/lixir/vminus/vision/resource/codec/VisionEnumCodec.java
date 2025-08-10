package net.lixir.vminus.vision.resource.codec;

import com.google.gson.*;
import com.google.gson.JsonParseException;
import net.lixir.vminus.vision.values.VisionValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionEnumCodec<E extends Enum<E>> extends VisionCodec<E> {
    @Override
    public Class<E> getClassType() {
        return enumType;
    }

    private final Class<E> enumType;

    public VisionEnumCodec(Class<E> enumType) {
        this.enumType = enumType;
    }

    @Override
    public @Nullable List<VisionValue<E>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionValue<E>> visionProperties = new ArrayList<>();
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

            VisionValue<E> visionValue = VisionValue.create(enumValue, arrayObject, jsonObject, key);
            visionProperties.add(visionValue);
        }

        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull E value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", value.name().toLowerCase());
        return jsonObject;
    }
}
