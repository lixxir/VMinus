package net.lixir.vminus.vision.resource.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.vision.util.VisionItemDecorator;
import net.lixir.vminus.vision.values.VisionValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionItemDecoratorCodec extends VisionCodec<VisionItemDecorator> {
    @Override
    public Class<VisionItemDecorator> getClassType() {
        return VisionItemDecorator.class;
    }

    @Override
    public @Nullable List<VisionValue<VisionItemDecorator>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionValue<VisionItemDecorator>> visionProperties = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            Float order = Mth.clamp(arrayObject.has("order") ? arrayObject.getAsJsonPrimitive("order").getAsFloat() : 0f, 0f, 200f);
            ResourceLocation resourceLocation = parseResourceLocation("texture", key, arrayObject);
            VisionItemDecorator visionItemDecorator = new VisionItemDecorator(resourceLocation, order);

            visionProperties.add(VisionValue.create(visionItemDecorator, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull VisionItemDecorator value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("texture", value.texture().toString());
        jsonObject.addProperty("order", value.order());
        return jsonObject;
    }
}
