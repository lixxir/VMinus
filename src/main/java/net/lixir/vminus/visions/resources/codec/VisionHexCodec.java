package net.lixir.vminus.visions.resources.codec;

import com.google.gson.*;
import net.lixir.vminus.visions.util.VisionItemDecorator;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionHexCodec extends AbstractVisionCodec<Integer> {
    @Override
    public @Nullable List<VisionValue<Integer>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<Integer>> visionValues = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            String value = arrayObject.getAsJsonPrimitive("value").getAsString();
            if (value.startsWith("#")) {
                value = value.substring(1);
            }
            int colorInt = Integer.parseInt(value, 16);

            visionValues.add(VisionValue.create(colorInt, arrayObject, jsonObject, key));
        }
        return visionValues;
    }

    @Override
    public @Nullable JsonObject encode(Integer value) {
        return encodeInteger(value);
    }
}
