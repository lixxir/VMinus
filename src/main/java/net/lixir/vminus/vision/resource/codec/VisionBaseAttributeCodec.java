package net.lixir.vminus.vision.resource.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.vision.util.VisionBaseAttribute;
import net.lixir.vminus.vision.values.VisionProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionBaseAttributeCodec extends VisionCodec<VisionBaseAttribute> {
    @Override
    public @Nullable List<VisionProperty<VisionBaseAttribute>> decode(JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<VisionBaseAttribute>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            double value;
            try {
                value = arrayObject.getAsJsonPrimitive("value").getAsDouble();
            } catch (Exception e) {
                throw new JsonParseException(key + " does not have a value.");
            }
            ResourceLocation resourceLocation = parseResourceLocation("id", key, arrayObject);
            Attribute attribute;
            try {
                attribute = ForgeRegistries.ATTRIBUTES.getValue(resourceLocation);
            } catch (Exception e) {
                throw new JsonParseException(key + " does not have an name.");
            }
            VisionBaseAttribute visionBaseAttribute = new VisionBaseAttribute(value, attribute);

            visionProperties.add(VisionProperty.create(visionBaseAttribute, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }
}
