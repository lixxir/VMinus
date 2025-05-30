package net.lixir.vminus.visions.resources.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.visions.util.VisionEntityVariant;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionEntityVariantCodec extends AbstractVisionCodec<VisionEntityVariant> {
    @Override
    public @Nullable List<VisionValue<VisionEntityVariant>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<VisionEntityVariant>> visionValues = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            if (!arrayObject.has("name"))
                throw new JsonParseException(key + " has no name.");
            ResourceLocation variantName = parseResourceLocation("name", key, arrayObject);
            Integer weight = arrayObject.has("weight") ? arrayObject.getAsJsonPrimitive("weight").getAsInt() : 1;
            if (!arrayObject.has("texture"))
                throw new JsonParseException(key + " has no texture.");
            ResourceLocation variantTexture = parseResourceLocation("texture", key, arrayObject);
            boolean replace = arrayObject.has("replace") && arrayObject.getAsJsonPrimitive("replace").getAsBoolean();
            VisionEntityVariant visionEntityVariant = new VisionEntityVariant(variantName, variantTexture, weight, replace);

            visionValues.add(VisionValue.create(visionEntityVariant, arrayObject, jsonObject, key));
        }
        return visionValues;
    }
}
