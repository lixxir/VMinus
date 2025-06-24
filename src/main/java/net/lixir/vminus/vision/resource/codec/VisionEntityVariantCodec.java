package net.lixir.vminus.vision.resource.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.vision.util.VisionCreativeOrder;
import net.lixir.vminus.vision.util.VisionEntityVariant;
import net.lixir.vminus.vision.values.VisionProperty;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionEntityVariantCodec extends VisionCodec<VisionEntityVariant> {
    @Override
    public Class<VisionEntityVariant> getClassType() {
        return VisionEntityVariant.class;
    }

    @Override
    public @Nullable List<VisionProperty<VisionEntityVariant>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<VisionEntityVariant>> visionProperties = new ArrayList<>();

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

            visionProperties.add(VisionProperty.create(visionEntityVariant, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull VisionEntityVariant value) {
        JsonObject jsonObject = new JsonObject();
        if (value.name() == null || value.texture() == null)
            return null;
        jsonObject.addProperty("name", value.name().toString());
        jsonObject.addProperty("texture", value.texture().toString());
        jsonObject.addProperty("weight", value.weight());
        if (value.replace()) {
            jsonObject.addProperty("replace", true);
        }
        return jsonObject;
    }
}
