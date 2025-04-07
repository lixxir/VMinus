package net.lixir.vminus.visions.resources.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.visions.util.VisionEntityVariant;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VisionEntityVariantParser extends AbstractVisionParser<VisionEntityVariant> {
    @Override
    public @Nullable List<VisionValue<VisionEntityVariant>> parse(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            if (!arrayObject.has("name"))
                throw new JsonParseException(key + " has no name.");
            String name = arrayObject.getAsJsonPrimitive("name").getAsString();
            Integer weight = arrayObject.has("weight") ? arrayObject.getAsJsonPrimitive("weight").getAsInt() : 1;
            ResourceLocation resourceLocation = parseResourceLocation("texture", key, arrayObject);
            boolean replace = arrayObject.has("replace") && arrayObject.getAsJsonPrimitive("replace").getAsBoolean();
            VisionEntityVariant visionEntityVariant = new VisionEntityVariant(name, resourceLocation, weight, replace);

            visionValues.add(VisionValue.create(visionEntityVariant, arrayObject, jsonObject, key));
        }
        return visionValues;
    }
}
