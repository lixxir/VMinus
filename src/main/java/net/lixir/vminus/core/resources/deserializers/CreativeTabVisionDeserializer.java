package net.lixir.vminus.core.resources.deserializers;

import com.google.gson.*;
import net.lixir.vminus.core.VisionType;
import net.lixir.vminus.core.resources.VisionProcessor;
import net.lixir.vminus.core.visions.CreativeTabVision;

import java.lang.reflect.Type;

public class CreativeTabVisionDeserializer implements JsonDeserializer<CreativeTabVision> {

    public CreativeTabVision deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        CreativeTabVision vision = new CreativeTabVision();
        vision.mergeEntries(VisionProcessor.getEntries(jsonObject, VisionType.CREATIVE_TAB));

        if (jsonObject.has(vision.hide.name()))
            VisionProcessor.parseBoolean(jsonObject, vision.hide.name(), vision.hide);
        if (jsonObject.has(vision.icon.name()))
            VisionProcessor.parseItemStack(jsonObject, vision.icon.name(), vision.icon);
        if (jsonObject.has(vision.remove.name()))
            VisionProcessor.parseItemStackWithTagKey(jsonObject, vision.remove.name(), vision.remove);
        if (jsonObject.has(vision.order.name()))
            VisionProcessor.parseCreativeOrder(jsonObject, vision.order.name(), vision.order);

        return vision;
    }

}