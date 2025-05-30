package net.lixir.vminus.visions.resources.codec;

import com.google.gson.*;
import net.lixir.vminus.visions.util.VisionEntityVariant;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionDoubleCodec extends AbstractVisionCodec<Double> {
    @Override
    public @Nullable List<VisionValue<Double>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<Double>> visionValues = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            double value = arrayObject.getAsJsonPrimitive("value").getAsDouble();

            visionValues.add(VisionValue.create(value, arrayObject, jsonObject, key));
        }
        return visionValues;
    }

    @Override
    public @Nullable JsonObject encode(Double value) {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(value);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("value", jsonPrimitive);
        return jsonObject;
    }
}
