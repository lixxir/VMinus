package net.lixir.vminus.visions.resources.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.visions.util.VisionItemDecorator;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionItemDecoratorCodec extends AbstractVisionCodec<VisionItemDecorator> {
    @Override
    public @Nullable List<VisionValue<VisionItemDecorator>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<VisionItemDecorator>> visionValues = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            Float order = Mth.clamp(arrayObject.has("order") ? arrayObject.getAsJsonPrimitive("order").getAsFloat() : 0f, 0f, 200f);
            ResourceLocation resourceLocation = parseResourceLocation("texture", key, arrayObject);
            VisionItemDecorator visionItemDecorator = new VisionItemDecorator(resourceLocation, order);

            visionValues.add(VisionValue.create(visionItemDecorator, arrayObject, jsonObject, key));
        }
        return visionValues;
    }
}
