package net.lixir.vminus.core.resources.deserializers;

import com.google.gson.*;
import net.lixir.vminus.core.VisionType;
import net.lixir.vminus.core.resources.VisionProcessor;
import net.lixir.vminus.core.visions.BlockVision;
import net.lixir.vminus.core.visions.EntityVision;

import java.lang.reflect.Type;

public class EntityVisionDeserializer implements JsonDeserializer<EntityVision> {

    public EntityVision deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        EntityVision vision = new EntityVision();
        vision.mergeEntries(VisionProcessor.getEntries(jsonObject, VisionType.ENTITY));

        return vision;
    }

}