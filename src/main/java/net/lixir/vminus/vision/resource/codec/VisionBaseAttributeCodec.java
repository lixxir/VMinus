package net.lixir.vminus.vision.resource.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.vision.util.BaseAttribute;
import net.lixir.vminus.vision.values.VisionValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionBaseAttributeCodec extends VisionCodec<BaseAttribute> {
    @Override
    public Class<BaseAttribute> getClassType() {
        return BaseAttribute.class;
    }

    @Override
    public @Nullable List<VisionValue<BaseAttribute>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionValue<BaseAttribute>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        if (jsonArray == null)
            throw new JsonParseException("Expected a JSON array for key: " + key);
        for (JsonElement jsonArrayElement : jsonArray) {
            if (!jsonArrayElement.isJsonObject())
                throw new JsonParseException("Expected a JSON object inside array for key: " + key);
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            double value;
            try {
                value = arrayObject.getAsJsonPrimitive("value").getAsDouble();
            } catch (Exception e) {
                throw new JsonParseException("Missing or invalid 'value' for key: " + key, e);
            }

            ResourceLocation resourceLocation = parseResourceLocation("id", key, arrayObject);
            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(resourceLocation);
            if (attribute == null) {
                throw new JsonParseException("Invalid attribute id for key '" + key + "': " + resourceLocation);
            }

            BaseAttribute baseAttribute = new BaseAttribute(value, attribute);
            visionProperties.add(VisionValue.create(baseAttribute, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull BaseAttribute value) {
        JsonObject jsonObject = new JsonObject();

        ResourceLocation key = ForgeRegistries.ATTRIBUTES.getKey(value.attribute());
        if (key == null)
            throw new IllegalArgumentException("Cannot encode VisionBaseAttribute with unknown attribute registry key");
        jsonObject.addProperty("id", key.toString());
        jsonObject.addProperty("value", value.value());

        return jsonObject;
    }
}
