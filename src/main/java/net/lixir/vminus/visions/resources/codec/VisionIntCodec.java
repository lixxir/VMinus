package net.lixir.vminus.visions.resources.codec;

import com.google.gson.*;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionIntCodec extends AbstractVisionCodec<Integer> {

    @Override
    public @Nullable List<VisionValue<Integer>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<Integer>> visionValues = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            int value = arrayObject.getAsJsonPrimitive("value").getAsInt();

            visionValues.add(VisionValue.create(value, arrayObject, jsonObject, key));
        }
        return visionValues;
    }

    @Override
    public @Nullable JsonObject encode(Integer value) {
        return encodeInteger(value);
    }
}
