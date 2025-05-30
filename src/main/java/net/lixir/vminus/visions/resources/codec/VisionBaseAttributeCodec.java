package net.lixir.vminus.visions.resources.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.visions.util.VisionBaseAttribute;
import net.lixir.vminus.visions.util.VisionEntityVariant;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionBaseAttributeCodec extends AbstractVisionCodec<VisionBaseAttribute> {
    @Override
    public @Nullable List<VisionValue<VisionBaseAttribute>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<VisionBaseAttribute>> visionValues = new ArrayList<>();

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

            visionValues.add(VisionValue.create(visionBaseAttribute, arrayObject, jsonObject, key));
        }
        return visionValues;
    }
}
