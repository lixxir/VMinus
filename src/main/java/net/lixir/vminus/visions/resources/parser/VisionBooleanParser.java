package net.lixir.vminus.visions.resources.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VisionBooleanParser extends AbstractVisionParser<Boolean> {
    @Override
    public @Nullable List<VisionValue<Boolean>> parse(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            boolean value = arrayObject.getAsJsonPrimitive("value").getAsBoolean();

            visionValues.add(VisionValue.create(value, arrayObject, jsonObject, key));
        }
        return visionValues;
    }
}
