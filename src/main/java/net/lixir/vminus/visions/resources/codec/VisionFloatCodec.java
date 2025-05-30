package net.lixir.vminus.visions.resources.codec;

import com.google.gson.*;
import net.lixir.vminus.visions.util.VisionFoodProperties;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionFloatCodec extends AbstractVisionCodec<Float> {
    @Override
    public @Nullable List<VisionValue<Float>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<Float>> visionValues = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            float value = arrayObject.getAsJsonPrimitive("value").getAsFloat();

            visionValues.add(VisionValue.create(value, arrayObject, jsonObject, key));
        }
        return visionValues;
    }

    @Override
    public @Nullable JsonObject encode(Float value) {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(value);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("value", jsonPrimitive);
        return jsonObject;
    }
}
