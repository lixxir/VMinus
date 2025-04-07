package net.lixir.vminus.visions.resources.parser;

import com.google.gson.*;
import com.google.gson.JsonParseException;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionEnumParser<E extends Enum<E>> extends AbstractVisionParser<E> {

    private final Class<E> enumType;

    public VisionEnumParser(Class<E> enumType) {
        this.enumType = enumType;
    }

    @Override
    public @Nullable List<VisionValue<E>> parse(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        List<VisionValue<E>> visionValues = new ArrayList<>();

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
            visionValues.add(visionValue);
        }

        return visionValues;
    }
}
