package net.lixir.vminus.core.resources.deserializers;

import com.google.gson.*;
import net.lixir.vminus.core.VisionType;
import net.lixir.vminus.core.resources.VisionProcessor;
import net.lixir.vminus.core.visions.EntityVision;

import java.lang.reflect.Type;

public class EntityVisionDeserializer implements JsonDeserializer<EntityVision> {

    public EntityVision deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        EntityVision vision = new EntityVision();
        vision.mergeEntries(VisionProcessor.getEntries(jsonObject, VisionType.ENTITY));

        if (jsonObject.has(vision.dampensVibrations.name()))
            VisionProcessor.parseBoolean(jsonObject, vision.dampensVibrations.name(), vision.dampensVibrations);
        if (jsonObject.has(vision.silent.name()))
            VisionProcessor.parseBoolean(jsonObject, vision.silent.name(), vision.silent);
        if (jsonObject.has(vision.ban.name()))
            VisionProcessor.parseBoolean(jsonObject, vision.ban.name(), vision.ban);
        if (jsonObject.has(vision.baseAttribute.name()))
            VisionProcessor.parseBaseAttribute(jsonObject, vision.baseAttribute.name(), vision.baseAttribute);

        return vision;
    }

}