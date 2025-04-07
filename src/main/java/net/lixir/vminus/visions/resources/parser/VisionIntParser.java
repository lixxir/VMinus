package net.lixir.vminus.visions.resources.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VisionIntParser extends AbstractVisionParser<Integer> {
    @Override
    public @Nullable List<VisionValue<Integer>> parse(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            int value = arrayObject.getAsJsonPrimitive("value").getAsInt();

            visionValues.add(VisionValue.create(value, arrayObject, jsonObject, key));
        }
        return visionValues;
    }
}
