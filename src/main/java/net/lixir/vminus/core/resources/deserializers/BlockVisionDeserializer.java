package net.lixir.vminus.core.resources.deserializers;

import com.google.gson.*;
import net.lixir.vminus.core.VisionType;
import net.lixir.vminus.core.resources.VisionProcessor;
import net.lixir.vminus.core.visions.BlockVision;

import java.lang.reflect.Type;

public class BlockVisionDeserializer implements JsonDeserializer<BlockVision> {

    public BlockVision deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        BlockVision vision = new BlockVision();
        vision.mergeEntries(VisionProcessor.getEntries(jsonObject, VisionType.BLOCK));

        if (jsonObject.has(vision.lightLevel.name()))
            VisionProcessor.parseInt(jsonObject, vision.lightLevel.name(), vision.lightLevel, 0, 15);
        if (jsonObject.has(vision.friction.name()))
            VisionProcessor.parseFloat(jsonObject, vision.friction.name(), vision.friction, Float.MIN_VALUE, Float.MAX_VALUE);
        if (jsonObject.has(vision.speedFactor.name()))
            VisionProcessor.parseFloat(jsonObject, vision.speedFactor.name(), vision.speedFactor, Float.MIN_VALUE, Float.MAX_VALUE);
        if (jsonObject.has(vision.jumpFactor.name()))
            VisionProcessor.parseFloat(jsonObject, vision.jumpFactor.name(), vision.jumpFactor, Float.MIN_VALUE, Float.MAX_VALUE);
        if (jsonObject.has(vision.destroySpeed.name()))
            VisionProcessor.parseFloat(jsonObject, vision.destroySpeed.name(), vision.destroySpeed, 0, Float.MAX_VALUE);
        if (jsonObject.has(vision.explosionResistance.name()))
            VisionProcessor.parseFloat(jsonObject, vision.explosionResistance.name(), vision.explosionResistance, 0, Float.MAX_VALUE);
        if (jsonObject.has(vision.emissive.name()))
            VisionProcessor.parseBoolean(jsonObject, vision.emissive.name(), vision.emissive);
        if (jsonObject.has(vision.redstoneConductor.name()))
            VisionProcessor.parseBoolean(jsonObject, vision.redstoneConductor.name(), vision.redstoneConductor);
        if (jsonObject.has(vision.occludes.name()))
            VisionProcessor.parseBoolean(jsonObject, vision.occludes.name(), vision.occludes);
        if (jsonObject.has(vision.sound.name()))
            VisionProcessor.parseSoundType(jsonObject, vision.sound.name(), vision.sound);

        return vision;
    }

}