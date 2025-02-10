package net.lixir.vminus.core.resources.deserializers;

import com.google.gson.*;
import net.lixir.vminus.core.VisionType;
import net.lixir.vminus.core.resources.VisionProcessor;
import net.lixir.vminus.core.visions.BlockVision;
import net.lixir.vminus.core.visions.ItemVision;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.UseAnim;

import java.lang.reflect.Type;

public class BlockVisionDeserializer implements JsonDeserializer<BlockVision> {

    public BlockVision deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        BlockVision vision = new BlockVision();
        vision.mergeEntries(VisionProcessor.getEntries(jsonObject, VisionType.BLOCK));

        if (jsonObject.has(vision.lightLevel.getName()))
            VisionProcessor.parseInt(jsonObject, vision.lightLevel.getName(), vision.lightLevel, 0, 15);
        if (jsonObject.has(vision.friction.getName()))
            VisionProcessor.parseFloat(jsonObject, vision.friction.getName(), vision.friction, Float.MIN_VALUE, Float.MAX_VALUE);
        if (jsonObject.has(vision.speedFactor.getName()))
            VisionProcessor.parseFloat(jsonObject, vision.speedFactor.getName(), vision.speedFactor, Float.MIN_VALUE, Float.MAX_VALUE);
        if (jsonObject.has(vision.jumpFactor.getName()))
            VisionProcessor.parseFloat(jsonObject, vision.jumpFactor.getName(), vision.jumpFactor, Float.MIN_VALUE, Float.MAX_VALUE);
        if (jsonObject.has(vision.destroySpeed.getName()))
            VisionProcessor.parseFloat(jsonObject, vision.destroySpeed.getName(), vision.destroySpeed, 0, Float.MAX_VALUE);
        if (jsonObject.has(vision.explosionResistance.getName()))
            VisionProcessor.parseFloat(jsonObject, vision.explosionResistance.getName(), vision.explosionResistance, 0, Float.MAX_VALUE);
        if (jsonObject.has(vision.emissive.getName()))
            VisionProcessor.parseBoolean(jsonObject, vision.emissive.getName(), vision.emissive);
        if (jsonObject.has(vision.redstoneConductor.getName()))
            VisionProcessor.parseBoolean(jsonObject, vision.redstoneConductor.getName(), vision.redstoneConductor);
        if (jsonObject.has(vision.occludes.getName()))
            VisionProcessor.parseBoolean(jsonObject, vision.occludes.getName(), vision.occludes);
        if (jsonObject.has(vision.sound.getName()))
            VisionProcessor.parseSoundType(jsonObject, vision.sound.getName(), vision.sound);

        return vision;
    }

}