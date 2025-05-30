package net.lixir.vminus.visions.resources.codec;

import com.google.gson.*;
import net.lixir.vminus.visions.util.VisionEntityVariant;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionRandomIntCodec extends AbstractVisionCodec<Integer> {
    @Override
    public @Nullable List<VisionValue<Integer>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<Integer>> visionValues = new ArrayList<>();

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


            visionValues.add(VisionValue.create(value, arrayObject, jsonObject, key));
        }
        return visionValues;
    }
}
