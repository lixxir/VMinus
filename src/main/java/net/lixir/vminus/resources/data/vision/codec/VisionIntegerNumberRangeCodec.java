package net.lixir.vminus.resources.data.vision.codec;

import com.google.gson.*;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.util.NumberRange;
import net.lixir.vminus.vision.values.VisionValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionIntegerNumberRangeCodec extends VisionCodec<NumberRange<Integer>> {
    @SuppressWarnings("unchecked")
    @Override
    public Class<NumberRange<Integer>> getClassType() {
        return (Class<NumberRange<Integer>>) (Class<?>) NumberRange.class;
    }

    @Override
    public @Nullable List<VisionValue<NumberRange<Integer>>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionValue<NumberRange<Integer>>> visionProperties = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);

        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            int min, max;
            try {
                min = arrayObject.getAsJsonPrimitive("min").getAsInt();
                max = arrayObject.getAsJsonPrimitive("max").getAsInt();
            } catch (Exception e) {
                VMinus.LOGGER.error("Failed parsing range in: {}", jsonObject);
                throw new JsonParseException(e);
            }

            NumberRange<Integer> numberRange = new NumberRange<>(min, max);
            visionProperties.add(VisionValue.create(numberRange, arrayObject, jsonObject, key));
        }

        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull NumberRange<Integer> numberRange) {
        JsonObject obj = new JsonObject();
        obj.addProperty("min", numberRange.min());
        obj.addProperty("max", numberRange.max());
        return obj;
    }
}
